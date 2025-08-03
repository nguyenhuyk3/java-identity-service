package com.identity.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity // Đánh dấu class này là một entity (tương ứng với một bảng trong database).
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id // Đánh dấu field id là primary key (khóa chính) của entity.
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;

    @ManyToMany
    Set<Role> roles;
}
