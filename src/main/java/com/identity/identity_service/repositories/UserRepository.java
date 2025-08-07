package com.identity.identity_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identity.identity_service.entities.User;

@Repository
/*
	1. @Repository
		Đây là annotation của Spring đánh dấu interface/class là repository – tầng truy cập dữ liệu (Data Access Layer).
		Spring sẽ tự động tạo bean cho interface này và quản lý dưới dạng Spring component, giống như @Service, @Controller.
	2. extends JpaRepository<User, String>
		Bạn đang kế thừa từ JpaRepository, là một interface cung cấp sẵn nhiều hàm thao tác với database.
			- User là tên của Entity class bạn muốn thao tác (đã được đánh dấu bằng @Entity)
			- String là kiểu dữ liệu của primary key (id trong entity User là kiểu String)
*/
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
