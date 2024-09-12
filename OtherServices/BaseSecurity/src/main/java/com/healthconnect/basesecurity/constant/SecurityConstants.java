package com.healthconnect.basesecurity.constant;

public class SecurityConstants {

    // JWT Token and Authorization
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String LOGIN_URI = "/api/v1/users/login";
    public static final String ISSUER_URI = "http://localhost:8080/realms/healthconnect-realm";

    // Role
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ADMIN";

    // Log messages
    public static final String STARTING_JWT_FILTER_LOG = "Starting JWT filter for request: {}";
    public static final String AUTH_HEADER_MISSING_LOG = "Authorization header is missing or does not start with 'Bearer'";
    public static final String JWT_TOKEN_EXTRACTED_LOG = "Extracted JWT token: {}";
    public static final String JWT_TOKEN_DECODED_LOG = "JWT token successfully decoded. Subject: {}";
    public static final String JWT_NO_ROLES_FOUND_LOG = "No roles found in JWT. Proceeding with no roles.";
    public static final String JWT_ROLES_EXTRACTED_LOG = "Extracted roles: {}";
    public static final String JWT_AUTH_SUCCESS_LOG = "JWT authentication successful for user: {} (Email: {})";
    public static final String JWT_AUTH_FAILED_LOG = "Failed to decode or authenticate JWT token";
    public static final String ACTIVE_USER_LOG = "Current active user: {}";
    public static final String ACTIVE_USER_AUTHORITIES_LOG = "Authorities: {}";
    public static final String NO_ACTIVE_USER_LOG = "No active user found in the security context.";
    public static final String REALM_ACCESS_LOG = "realm_access claim: {}";
    public static final String NO_ROLES_REALM_ACCESS_LOG = "No roles found in realm_access. Checking resource_access...";
    public static final String ROLES_EXTRACTED_LOG = "Roles extracted from JWT: {}";

    // Keycloak Resources
    public static final String REALM_ACCESS = "realm_access";
    public static final String ROLES = "roles";
    public static final String RESOURCE_ACCESS = "resource_access";
    public static final String TOKEN_NOT_FOUND_LOG = "Token not found in redis.";
    public static final String ROLES_DESERIALIZE_ERROR_LOG = "Error Deserializing roles.";
    public static final String TOKEN_DATA_FOUND_REDIS_LOG = "Token Data found in Redis.";
    public static final String REDIS_ACCESS_FAILED_LOG = "Failed to access redis.";
    public static final String JWT_TOKEN_SAME_LOG = "Jwt Token Matches.";


    private SecurityConstants() {
        // Private constructor to prevent instantiation
    }
}
