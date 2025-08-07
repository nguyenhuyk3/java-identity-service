package com.identity.identity_service.controllers;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.identity.identity_service.dto.requests.UserCreationRequest;
import com.identity.identity_service.dto.responses.UserResponse;
import com.identity.identity_service.services.UserService;

import lombok.extern.slf4j.Slf4j;

/*
		- @Slf4j là một annotation của thư viện Lombok trong Java,
	dùng để tự động tạo ra một logger cho class mà bạn gắn annotation này, giúp bạn không phải viết thủ công phần khai báo logger nữa.
		- @SpringBootTest là một annotation trong Spring Boot dùng để chạy test với toàn bộ context của ứng dụng Spring Boot.
		- @AutoConfigureMockMvc là annotation trong Spring Boot Test, dùng để tự động cấu hình MockMvc
	để bạn có thể test controller mà không cần khởi động server thật.
		- @Testcontainers là annotation của thư viện Testcontainers Java dùng để quản lý vòng đời container Docker khi chạy test trong Java.
			1. Chức năng
				+ Cho phép khởi chạy container Docker (PostgreSQL, MySQL, Redis, Kafka, Elasticsearch…) ngay trong test.
				+ @Testcontainers báo cho JUnit biết rằng class này có sử dụng container và nó cần được khởi động & dừng đúng lúc.
				+ Giúp test integration trên môi trường giống production hơn, thay vì mock database hoặc service.
*/
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource("/test.properties")
public class UserControllerTest {
    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest");
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> MY_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username", () -> MY_SQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password", () -> MY_SQL_CONTAINER.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dateOfBirth;

    @BeforeEach
    void initData() {
        dateOfBirth = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dateOfBirth(dateOfBirth)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dateOfBirth)
                .build();
    }

    @Test
    //
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createNewUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("cf0600f538b3"));
    }

    @Test
    //
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        request.setUsername("joh");

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 4 characters"));
    }
}
