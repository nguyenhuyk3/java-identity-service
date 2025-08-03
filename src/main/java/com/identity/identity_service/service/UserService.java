package com.identity.identity_service.service;

import com.identity.identity_service.dto.request.UserCreationRequest;
import com.identity.identity_service.dto.request.UserUpdateRequest;
import com.identity.identity_service.dto.response.UserResponse;
import com.identity.identity_service.entity.User;
import com.identity.identity_service.enums.Role;
import com.identity.identity_service.exception.AppException;
import com.identity.identity_service.exception.ErrorCode;
import com.identity.identity_service.mapper.UserMapper;
import com.identity.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
// @Service là annotation dùng để đánh dấu một class là Service (lớp xử lý logic nghiệp vụ) và được quản lý bởi Spring container.
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    //    @Autowired // @Autowired dùng để tự động tiêm (inject) một bean vào class đang sử dụng.
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

//    public User createNewUser(UserCreationRequest req) {
//        User newUser = new User();
//
//        if (userRepository.existsByUsername(req.getUsername())) {
//            throw new AppException(ErrorCode.USER_EXISTED);
//        }
//
//        newUser.setUsername(req.getUsername());
//        newUser.setPassword(req.getPassword());
//        newUser.setFirstName(req.getFirstName());
//        newUser.setLastName(req.getLastName());
//        newUser.setDateOfBirth(req.getDateOfBirth());
//
//        /*
//            S extends T nghĩa là gì?
//            -> <S extends T> S save(S entity)
//            -> Nghĩa là: bạn có thể lưu bất kỳ thực thể nào thuộc loại T hoặc con của T
//        */
//        return userRepository.save(newUser);
//    }

    public UserResponse createNewUser(UserCreationRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User newUser = userMapper.toUser(req);

        newUser.setPassword(passwordEncoder.encode(req.getPassword()));

        HashSet<String> roles = new HashSet<>();

        roles.add(Role.USER.name());

        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String userName = context.getAuthentication().getName();

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    /*
        returnObject: Đối tượng được trả về từ method.
        authentication.name: Lấy username của người dùng hiện tại đang đăng nhập,
                                tức SecurityContextHolder.getContext().getAuthentication().getName()
    */
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper
                .toUserResponse(userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String id, UserUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, req);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
}
