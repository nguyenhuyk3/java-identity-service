package com.identity.identity_service.controller;

import com.identity.identity_service.dto.response.APIResponse;
import com.identity.identity_service.dto.request.UserCreationRequest;
import com.identity.identity_service.dto.request.UserUpdateRequest;
import com.identity.identity_service.dto.response.UserResponse;
import com.identity.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
/*
    @RestController
        - Đây là một annotation của Spring Boot.
        - Nó kết hợp giữa @Controller và @ResponseBody, giúp bạn xây dựng RESTful API.
        - Tự động chuyển giá trị trả về từ các method thành JSON (hoặc XML nếu được cấu hình).
    @RequestMapping("/users")
        - Xác định rằng mọi endpoint bên trong lớp này sẽ bắt đầu với /users.
        - Ví dụ: POST /users sẽ gọi tới method createUser(...).
*/
@RequiredArgsConstructor()
/*
    Được dùng để tự động sinh constructor cho class Java, với các field final hoặc @NonNull.
*/
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
/*
    Annotation @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) là của Lombok,
dùng để thiết lập mặc định cho tất cả các field trong class.
    Mục đích:
        - level = AccessLevel.PRIVATE: tất cả các field trong class sẽ mặc định là private.
        - makeFinal = true: tất cả các field sẽ mặc định là final.
*/
@Slf4j
public class UserController {
    //    @Autowired
    UserService userService;

    @PostMapping
    APIResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest req) {
        return APIResponse.<UserResponse>builder()
                .result(userService.createNewUser(req))
                .build();
    }

    @GetMapping
    APIResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        authentication
                .getAuthorities()
                .forEach(
                        grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return APIResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }


    @GetMapping("/{userId}")
    APIResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    APIResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return APIResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    APIResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return APIResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }
}
