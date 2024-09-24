package com.smw.SocialMediaWeb.configuration;

import com.smw.SocialMediaWeb.entity.Role;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.mapper.RoleMapper;
import com.smw.SocialMediaWeb.repository.RoleRepository;
import com.smw.SocialMediaWeb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                Role roles = roleRepository.findById("ADMIN").orElse(Role.builder()
                        .name("ADMIN")
                        .description("Admin role")
                        .build());
                if(!(roles.getName().equalsIgnoreCase("admin")))
                    roleRepository.save(roles);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(Set.of(roles))
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password, please change it");
            }
        };
    }
}
