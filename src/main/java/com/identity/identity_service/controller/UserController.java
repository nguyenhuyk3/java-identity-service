package com.identity.identity_service.controller;

import com.identity.identity_service.dto.request.APIResponse;
import com.identity.identity_service.dto.request.UserCreationRequest;
import com.identity.identity_service.dto.request.UserUpdateRequest;
import com.identity.identity_service.entity.User;
import com.identity.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {
//    @Autowired
    UserService userService;

    @PostMapping
    User createUser(@RequestBody @Valid UserCreationRequest req) {
        APIResponse res = new APIResponse();

        res.setResult(userService.createNewUser(req));

        return userService.createNewUser(req);
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return "User has been deleted";
    }
}
