package com.identity.identity_service.dto.requests;

import com.identity.identity_service.validators.DOBConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;

    @DOBConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;

    List<String> roles;
}
