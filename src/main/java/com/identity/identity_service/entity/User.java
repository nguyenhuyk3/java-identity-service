package com.identity.identity_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity // Đánh dấu class này là một entity (tương ứng với một bảng trong database).
@Table(name = "user")
public class User {
    @Id // Đánh dấu field id là primary key (khóa chính) của entity.
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
