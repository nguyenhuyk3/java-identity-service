package com.identity.identity_service.configurations;

import java.time.LocalDate;
import java.util.HashSet;

import com.identity.identity_service.constants.PredefinedRole;
import com.identity.identity_service.mappers.UserMapper;
import com.identity.identity_service.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.identity.identity_service.entities.User;
import com.identity.identity_service.entities.Role;
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
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    public ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        System.out.println(SECRET_USERNAME);
        return args -> {
            if (userRepository.findByUsername(SECRET_USERNAME).isEmpty()) {
                roleRepository.save(Role
                        .builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());


                Role adminRole = roleRepository.save(Role
                        .builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();

                roles.add(adminRole);

                User adminUser = User
                        .builder()
                        .username(SECRET_USERNAME)
                        .password(passwordEncoder.encode(SECRET_PASSWORD))
                        .firstName(SECRET_USERNAME)
                        .lastName(SECRET_USERNAME)
                        .dateOfBirth(LocalDate.parse("2003-11-16"))
                        .roles(roles)
                        .build();

                userRepository.save(adminUser);
            }
        };
    }
}
