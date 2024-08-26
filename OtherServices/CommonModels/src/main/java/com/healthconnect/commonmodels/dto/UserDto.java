package com.healthconnect.commonmodels.dto;

import com.healthconnect.commonmodels.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private LocalDateTime createdAt;

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;

    private Gender gender;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{13}$", message = "CNIC must be 13 digits")
    private String cnic;

    private String address;
    private String city;
    private Boolean isActive;
    private String keycloakUserId;
}
