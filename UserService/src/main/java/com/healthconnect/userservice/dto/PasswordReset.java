package com.healthconnect.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordReset {
    @NotBlank(message = "Reset token is mandatory")
    private String token;

    @NotBlank(message = "New password is mandatory")
    private String newPassword;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;
}
