package com.identity.identity_service.configurations;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.identity.identity_service.entities.User;
import com.identity.identity_service.enums.Role;
import com.identity.identity_service.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

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
    /*
    	Nó được dùng để chỉ tạo bean hoặc cấu hình nếu một property nào đó trong application.properties (hoặc application.yml) thỏa điều kiện.
    		+ prefix = "spring" → phần đầu của key property.
    		+ value = "datasource.driverClassName" → phần sau của key property.
    		+ Ghép lại: spring.datasource.driverClassName
    		+ havingValue = "com.mysql.cj.jdbc.Driver" → chỉ kích hoạt bean nếu giá trị của property bằng giá trị này.
    */
//    @ConditionalOnProperty(
//            prefix = "spring",
//            value = "datasource.driverClassName",
//            havingValue = "com.mysql.cj.jdbc.Driver")
    public ApplicationRunner applicationRunner(UserRepository userRepository) {
        System.out.println(SECRET_USERNAME);
        return args -> {
            if (userRepository.findByUsername(SECRET_USERNAME).isEmpty()) {
                var roles = new HashSet<String>();

                roles.add(Role.ADMIN.name());

                User adminUser = User.builder()
                        .username(SECRET_USERNAME)
                        .password(passwordEncoder.encode(SECRET_PASSWORD))
                        //                        .roles(roles)
                        .build();

                userRepository.save(adminUser);
            }
        };
    }
}
