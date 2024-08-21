package com.healthconnect.userservice.dto;

import com.healthconnect.userservice.constant.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginCredentials {
    @Email(message = ValidationMessages.EMAIL_VALID)
    @NotBlank(message = ValidationMessages.EMAIL_MANDATORY)
    private String email;

    @NotBlank(message = ValidationMessages.PASSWORD_MANDATORY)
    private String password;
}
