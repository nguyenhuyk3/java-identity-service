package com.identity.identity_service.configuration;

import com.identity.identity_service.entity.User;
import com.identity.identity_service.enums.Role;
import com.identity.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfiguration {
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${admin.secretUsername}")
    String SECRET_USERNAME;

    @NonFinal
    @Value("${admin.secretPassword}")
    String SECRET_PASSWORD;

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername(SECRET_USERNAME).isEmpty()) {
                var roles = new HashSet<String>();

                roles.add(Role.ADMIN.name());

                User adminUser = User.builder()
                        .username(SECRET_USERNAME)
                        .password(passwordEncoder.encode(SECRET_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(adminUser);
            }
        };
    }
}
