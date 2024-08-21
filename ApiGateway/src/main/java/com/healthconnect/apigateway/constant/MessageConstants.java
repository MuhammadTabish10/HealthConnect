package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstants {

    // Log Messages
    public static final String PROCESSING_REQUEST_LOG = "Processing request for URI: %s";
    public static final String AUTH_HEADER_MISSING_OR_INVALID = "Authorization header is missing or does not start with Bearer";
    public static final String EXTRACTED_JWT_TOKEN_LOG = "Extracted JWT Token: %s";
    public static final String JWT_VALID_LOG = "JWT Token is valid. Extracted email: %s";
    public static final String JWT_INVALID_LOG = "JWT Token is invalid or expired";
    public static final String JWT_VALIDATION_EXCEPTION_LOG = "Exception occurred while validating JWT Token: %s";
    public static final String JWT_MISSING_ERROR = "JWT Token is missing or invalid";
    public static final String CONFIGURING_FILTER_CHAIN = "Configuring security filter chain";
    public static final String CONFIGURING_AUTHORIZATION_EXCHANGE = "Configuring authorization for exchanges";
    public static final String CONFIGURING_OAUTH2_SERVER = "Configuring OAuth2 resource server";
    public static final String CONFIGURING_REACTIVE_JWT_DECODER = "Creating ReactiveJwtDecoder with issuer URI: {}";
    public static final String CONFIGURING_JWT_AUTHENTICATION_CONVERTER = "Configuring JWT authentication converter";

    // JwtUtils Log Messages
    public static final String SIGNING_KEY_INITIALIZED = "Signing key initialized";
    public static final String JWT_TOKEN_EXPIRED = "JWT Token is expired";
    public static final String JWT_VALIDATION_FAILED = "JWT Token validation failed: %s";
    public static final String EXTRACTED_CLAIM_LOG = "Extracted claim: %s";
    public static final String EXTRACTED_USERNAME_LOG = "Extracted username: %s";

    // RouteValidator Log Messages
    public static final String REQUEST_URI_SECURED_LOG = "Request for URI %s is secured: %s";

    // GatewayConfig Log Messages
    public static final String SETTING_UP_ROUTES_LOG = "Setting up API Gateway routes";

}
