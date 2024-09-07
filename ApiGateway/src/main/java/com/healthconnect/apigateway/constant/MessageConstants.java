package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstants {

    // Log Messages
    public static final String CONFIGURING_FILTER_CHAIN = "Configuring security filter chain";
    public static final String CONFIGURING_AUTHORIZATION_EXCHANGE = "Configuring authorization for exchanges";
    public static final String CONFIGURING_OAUTH2_SERVER = "Configuring OAuth2 resource server";
    public static final String CONFIGURING_REACTIVE_JWT_DECODER = "Creating ReactiveJwtDecoder with issuer URI: {}";
    public static final String CONFIGURING_JWT_AUTHENTICATION_CONVERTER = "Configuring JWT authentication converter";
    public static final String PUBLIC_ROUTE_LOG = "Public route request, skipping token validation.";
    public static final String VALID_TOKEN_LOG = "Token data is valid and exists in Redis, skipping storage.";
    public static final String EXPIRED_TOKEN_LOG = "Token in Redis is expired, removing.";
    public static final String STORING_TOKEN_LOG = "Storing new token data in Redis: {}, {}";
    public static final String ERROR_SERIALIZING_ROLES_LOG = "Error serializing roles: {}";
    public static final String ERROR_REDIS_LOG = "Error handling Redis token storage: {}";



    // Roles Log Messages
    public static final String REALM_ACCESS_LOG = "realm_access claim: {}";
    public static final String NO_ROLES_REALM_ACCESS_LOG = "No roles found in realm_access. Checking resource_access...";
    public static final String ROLES_EXTRACTED_LOG = "Roles extracted from JWT: {}";

    // RouteValidator Log Messages
    public static final String REQUEST_URI_SECURED_LOG = "Request for URI %s is secured: %s";

    // GatewayConfig Log Messages
    public static final String SETTING_UP_ROUTES_LOG = "Setting up API Gateway routes";
    public static final String REDIS_RATE_LIMITER_INITIALIZING = "RedisRateLimiter is being initialized.";
    public static final String KEY_RESOLVER_INITIALIZING = "KeyResolver is being initialized.";
    public static final String REDIS_CONNECTION_SUCCESS = "Redis is connected and functional.";
    public static final String REDIS_CONNECTION_FAILURE = "Redis might not be connected. Please check Redis connection.";
    public static final String REDIS_CONNECTION_ERROR = "Error connecting to Redis: ";

}
