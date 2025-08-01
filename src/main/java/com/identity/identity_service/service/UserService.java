package com.identity.identity_service.service;

import com.identity.identity_service.dto.request.UserCreationRequest;
import com.identity.identity_service.dto.request.UserUpdateRequest;
import com.identity.identity_service.entity.User;
import com.identity.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //@Service là annotation dùng để đánh dấu một class là Service (lớp xử lý logic nghiệp vụ) và được quản lý bởi Spring container.
public class UserService {
    @Autowired // @Autowired dùng để tự động tiêm (inject) một bean vào class đang sử dụng.
    private UserRepository userRepository;

    public User createNewUser(UserCreationRequest req) {
        User newUser = new User();

        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        newUser.setUsername(req.getUsername());
        newUser.setPassword(req.getPassword());
        newUser.setFirstName(req.getFirstName());
        newUser.setLastName(req.getLastName());
        newUser.setDateOfBirth(req.getDateOfBirth());

        /*
            S extends T nghĩa là gì?
            -> <S extends T> S save(S entity)
            -> Nghĩa là: bạn có thể lưu bất kỳ thực thể nào thuộc loại T hoặc con của T
        */
        return userRepository.save(newUser);
    }

    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(String id, UserUpdateRequest req) {
        User user = getUser(id);

        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDateOfBirth(req.getDateOfBirth());

        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
