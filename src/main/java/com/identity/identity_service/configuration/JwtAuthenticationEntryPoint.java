package com.identity.identity_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.identity.identity_service.dto.response.APIResponse;
import com.identity.identity_service.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        res.setStatus(errorCode.getStatusCode().value());
        /*
            - Mục đích:
                + Khai báo rằng response trả về cho client có định dạng là JSON.
            - Chi tiết:
                + MediaType.APPLICATION_JSON_VALUE tương đương với chuỗi "application/json".
        */
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        APIResponse<?> apiResponse = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        /*
            - Mục đích:
                + Tạo một instance của ObjectMapper – đây là một class của thư viện Jackson,
                dùng để chuyển đổi giữa Java Object và JSON.
        */
        ObjectMapper objectMapper = new ObjectMapper();
        /*
            objectMapper.writeValueAsString(apiResponse) chuyển object apiResponse thành chuỗi JSON.
            res.getWriter().write(...) ghi chuỗi đó vào response body.
        */
        res.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        res.flushBuffer();
    }
}
