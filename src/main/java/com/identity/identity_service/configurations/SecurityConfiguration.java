package com.identity.identity_service.configurations;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
/*
    @Configuration
    - Mục đích:
        + Đánh dấu class hiện tại là class cấu hình (configuration class) trong Spring.
        + Tương đương với việc thay thế file XML cấu hình (applicationContext.xml).
    - Ý nghĩa:
        + Spring sẽ quét và khởi tạo các @Bean trong class này để đưa vào Spring Container (IoC container).
        + Ví dụ: bạn viết @Bean public PasswordEncoder passwordEncoder(), Spring sẽ gọi method đó và quản lý bean trả về.
    @EnableWebSecurity
    - Mục đích:
        + Bật Spring Security cho ứng dụng web.
        + Import cấu hình mặc định của Spring Security.
    - Ý nghĩa:
        + Khi bạn thêm annotation này, Spring sẽ tự động tạo ra một SecurityFilterChain mặc định,
    trừ khi bạn định nghĩa @Bean SecurityFilterChain của riêng bạn (custom).
        + Nếu không thêm @EnableWebSecurity, Spring Boot vẫn bật security mặc định,
    nhưng bạn không thể cấu hình chi tiết được.
*/
@EnableMethodSecurity
/*
        - @EnableMethodSecurity là annotation của Spring Security dùng để bật bảo mật ở cấp độ method
    — tức là cho phép sử dụng các annotation như:
            + @PreAuthorize
            + @PostAuthorize
            + @Secured
            + @RolesAllowed
        - Ý nghĩa:
            + Annotation này bật AOP proxy để can thiệp vào các method (controller/service)
        và kiểm tra quyền truy cập.
            + Nếu bạn không thêm @EnableMethodSecurity, thì @PreAuthorize
        và các annotation tương tự sẽ không hoạt động, dù bạn có cấu hình Security đúng.
*/
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfiguration {
    String[] PUBLIC_ENDPOINTS = {
            "/users",
            "/auth/token",
            "/auth/introspect"
    };

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        /*
            permitAll() nghĩa là không cần đăng nhập vẫn truy cập được.
            .anyRequest().authenticated(): tất cả các request khác đều yêu cầu
        xác thực (phải có JWT hợp lệ).
        */
        httpSecurity.authorizeHttpRequests(
                req -> req
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());
        /*
            Kích hoạt OAuth2 Resource Server mode: tức là ứng dụng của bạn không phát hành token,
        mà chỉ nhận và xác minh token từ client.
            jwt().decoder(jwtDecoder()): sử dụng 1 hàm jwtDecoder() do bạn định nghĩa riêng để giải mã và xác thực token JWT.
        */
//        httpSecurity.oauth2ResourceServer(
//                oauth2 -> oauth2
//                        .jwt(
//                                jwtConfigurer -> jwtConfigurer
//                                        .decoder(jwtDecoder())));
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer
                                        .decoder(jwtDecoder())
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Tạo ra một đối tượng chuyên dùng để lấy danh sách quyền (authorities) từ claim trong JWT.
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        /*
           Nói rằng mỗi quyền (authority) lấy từ JWT sẽ được thêm tiền tố "ROLE_".
           Ví dụ:
                - Nếu trong JWT có roles: ["ADMIN"], thì sau khi convert, bạn sẽ nhận được quyền: ROLE_ADMIN
                - Điều này giúp bạn dùng hasRole("ADMIN") trong Spring Security
        */
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        // Gán converter vào JwtAuthenticationConverter, để khi Spring phân tích JWT, nó biết phải lấy quyền từ đâu và thêm tiền tố thế nào.
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
