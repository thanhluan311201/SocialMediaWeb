package com.smw.SocialMediaWeb.configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private CustomJwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> subProtocols = accessor.getNativeHeader("Sec-WebSocket-Protocol");

            if (subProtocols != null && !subProtocols.isEmpty()) {
                String token = extractTokenFromSubProtocol(subProtocols.get(0));
                if (token != null) {
                    try {
                        Jwt decodedToken = jwtDecoder.decode(token);
                        String username = decodedToken.getClaimAsString("sub");

                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                username, null, null);
                        accessor.setUser(auth);

                    } catch (JwtException e) {
                        throw new JwtException("Token authentication failed: " + e.getMessage());
                    }
                } else {
                    throw new JwtException("Invalid subprotocol format");
                }
            } else {
                throw new JwtException("Missing authentication token in subprotocol");
            }
        }
        return message;
    }

    private String extractTokenFromSubProtocol(String subProtocol) {
        // Kiểm tra định dạng subprotocol và trích xuất token
        if (subProtocol.startsWith("base64url.bearer.authorization.k8s.io.")) {
            String encodedToken = subProtocol.substring(subProtocol.lastIndexOf('.') + 1);
            // Giải mã base64url (có thể cần thêm padding)
            return new String(java.util.Base64.getUrlDecoder().decode(encodedToken));
        }
        return null; // hoặc xử lý lỗi
    }
}
