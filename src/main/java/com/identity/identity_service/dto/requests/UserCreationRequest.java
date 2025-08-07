package com.identity.identity_service.dto.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.identity.identity_service.validators.DOBConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    @DOBConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dateOfBirth;
}
