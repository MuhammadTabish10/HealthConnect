package com.healthconnect.userservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.userservice.dto.LoginCredentials;
import com.healthconnect.userservice.dto.TokenResponse;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.healthconnect.userservice.constant.LogMessages.USER_NOT_FOUND_IN_KEYCLOAK;
import static com.healthconnect.userservice.constant.UserConstants.*;

@Component
public class UserUtils {

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    private final ObjectMapper objectMapper;
    private final Keycloak keycloak;

    public UserUtils(ObjectMapper objectMapper, Keycloak keycloak) {
        this.objectMapper = objectMapper;
        this.keycloak = keycloak;
    }

    public static String extractUserIdFromResponse(Response response) {
        return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
    }

    public String extractErrorDescription(String responseBody) {
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});
            return (String) errorResponse.getOrDefault("error_description", "An error occurred");
        } catch (Exception ex) {
            return responseBody;
        }
    }

    public void enableOrDisableUser(String realm, String userId, Boolean isActive) {
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        if (user != null) {
            user.setEnabled(isActive);
            keycloak.realm(realm).users().get(userId).update(user);
        } else {
            throw new EntityNotFoundException(USER_NOT_FOUND_IN_KEYCLOAK);
        }
    }

    public void resetUserPassword(String userId, String newPassword) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);

        userResource.resetPassword(credential);
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

    public String generateResetToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[4];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes).substring(0, 6);
    }
}
