package com.healthconnect.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
}
