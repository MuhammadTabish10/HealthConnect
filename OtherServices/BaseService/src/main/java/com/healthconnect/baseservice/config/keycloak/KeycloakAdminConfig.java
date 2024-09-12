package com.healthconnect.baseservice.config.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Value("${app.keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Value("${app.keycloak.resource}")
    private String clientId;

    @Value("${app.keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${app.keycloak.credentials.username}")
    private String adminUsername;

    @Value("${app.keycloak.credentials.password}")
    private String adminPassword;

    @Bean
    public Keycloak keycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}