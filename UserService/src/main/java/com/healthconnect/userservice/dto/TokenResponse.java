package com.healthconnect.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private LocalDateTime  expiresIn;
    private String refreshToken;
    private LocalDateTime refreshExpiresIn;
    private String tokenType;
    private String sessionState;
    private String scope;
    private LocalDateTime generatedAt;
}
