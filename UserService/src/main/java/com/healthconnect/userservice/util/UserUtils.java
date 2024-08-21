package com.healthconnect.userservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.userservice.dto.LoginCredentials;
import com.healthconnect.userservice.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.healthconnect.userservice.constant.UserConstants.*;

@Component
public class UserUtils {

    @Value("${app.client-id}")
    private String clientId;

    @Value("${app.client-secret}")
    private String clientSecret;

    private final ObjectMapper objectMapper;

    public UserUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractErrorDescription(String responseBody) {
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});
            return (String) errorResponse.getOrDefault("error_description", "An error occurred");
        } catch (Exception ex) {
            return responseBody;
        }
    }

    public HttpHeaders createFormUrlEncodedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public TokenResponse buildTokenResponse(HashMap<String, Object> responseBody) {
        return TokenResponse.builder()
                .accessToken((String) responseBody.get(ACCESS_TOKEN))
                .expiresIn(LocalDateTime.now().plusSeconds(((Number) responseBody.get(EXPIRES_IN)).longValue()))
                .refreshToken((String) responseBody.get(REFRESH_TOKEN))
                .refreshExpiresIn(LocalDateTime.now().plusSeconds(((Number) responseBody.get(REFRESH_EXPIRES_IN)).longValue()))
                .tokenType((String) responseBody.get(TOKEN_TYPE))
                .sessionState((String) responseBody.get(SESSION_STATE))
                .scope((String) responseBody.get(SCOPE))
                .generatedAt(LocalDateTime.now())
                .build();
    }

    public MultiValueMap<String, String> createLogoutRequestBody(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(CLIENT_ID, clientId);
        body.add(REFRESH_TOKEN, refreshToken);
        body.add(CLIENT_SECRET, clientSecret);
        return body;
    }

    public MultiValueMap<String, String> createRefreshTokenRequestBody(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE, REFRESH_TOKEN);
        body.add(CLIENT_ID, clientId);
        body.add(REFRESH_TOKEN, refreshToken);
        body.add(CLIENT_SECRET, clientSecret);
        return body;
    }

    public MultiValueMap<String, String> createLoginRequestBody(LoginCredentials loginCredentials) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(USERNAME, loginCredentials.getEmail());
        body.add(PASSWORD, loginCredentials.getPassword());
        body.add(GRANT_TYPE, PASSWORD);
        body.add(CLIENT_ID, clientId);
        body.add(CLIENT_SECRET, clientSecret);
        return body;
    }
}
