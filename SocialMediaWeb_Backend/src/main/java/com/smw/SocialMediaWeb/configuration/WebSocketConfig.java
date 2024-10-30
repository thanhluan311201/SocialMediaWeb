package com.smw.SocialMediaWeb.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomJwtDecoder jwtDecoder;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user")
                .setHeartbeatValue(new long[]{10000, 10000}).setTaskScheduler(heartBeatScheduler());
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000");
//                .addInterceptors(new WebSocketAuthInterceptor())
//                .setHandshakeHandler(customHandshakeHandler());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);

        return false;
    }

    @Bean
    public ThreadPoolTaskScheduler heartBeatScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);  // Thiết lập số luồng
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        scheduler.initialize();
        return scheduler;
    }

    @Bean
    public HandshakeHandler customHandshakeHandler() {
        return new DefaultHandshakeHandler() {

            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                List<String> subProtocols = request.getHeaders().get("sec-websocket-protocol");
                if (subProtocols != null && !subProtocols.isEmpty()) {
                    String protocol = subProtocols.get(0);
                    // Xử lý protocol nếu cần, ví dụ như log protocol
                    log.info("Received subprotocol: {}", protocol);
                }

                // Sử dụng thông tin từ SecurityContext hoặc tạo một đối tượng Principal tùy chỉnh
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), auth.getAuthorities());
                }
                return null;
            }
        };
    }

    public class WebSocketAuthInterceptor extends HttpSessionHandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request,
                                       ServerHttpResponse response,
                                       WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) throws Exception {
            log.info("Attempting WebSocket handshake");
            List<String> subProtocols = request.getHeaders().get("sec-websocket-protocol");
            if (subProtocols != null && !subProtocols.isEmpty()) {
                String token = extractTokenFromSubProtocol(subProtocols.get(0));
                log.info(subProtocols.toString());
                log.info("Extracted token: {}", token);
                if (token != null) {
                    try {
                        Jwt decodedToken = jwtDecoder.decode(token);
                        String username = decodedToken.getClaimAsString("sub");
                        // Thiết lập thuộc tính cho phiên người dùng
                        attributes.put("username", username);
                        log.info("username: {}", username);
                        return true; // Cho phép kết nối
                    } catch (JwtException e) {
                        log.error("Token authentication failed: {}", e.getMessage());
                        log.info("fail");
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return false; // Từ chối kết nối
                    }
                } else {
                    log.error("Invalid subprotocol format");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false; // Từ chối kết nối
                }
            } else {
                log.error("Missing authentication token in subprotocol");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false; // Từ chối kết nối
            }
        }
    }

    private String extractTokenFromSubProtocol(String subProtocol) {
        // Kiểm tra định dạng subprotocol và trích xuất token
        if (subProtocol.startsWith("base64url.bearer.authorization.k8s.io.")) {
            String encodedToken = subProtocol.substring("base64url.bearer.authorization.k8s.io.".length());
            // Giải mã base64url (có thể cần thêm padding)
            return new String(Base64.getUrlDecoder().decode(encodedToken));
        }
        return null; // hoặc xử lý lỗi
    }

}
