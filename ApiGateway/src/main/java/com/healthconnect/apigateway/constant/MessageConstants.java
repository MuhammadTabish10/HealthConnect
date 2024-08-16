package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstants {

//    // Log Messages
    public static final String REQUEST_LOG_HEADER = "--------------- Request ---------------";
    public static final String RESPONSE_LOG_HEADER = "--------------- Response ---------------";
    public static final String PROCESSING_REQUEST_LOG = "Processing request for URI: %s";
    public static final String AUTH_HEADER_MISSING_OR_INVALID = "Authorization header is missing or does not start with Bearer";
    public static final String EXTRACTED_JWT_TOKEN_LOG = "Extracted JWT Token: %s";
    public static final String JWT_VALID_LOG = "JWT Token is valid. Extracted email: %s";
    public static final String JWT_INVALID_LOG = "JWT Token is invalid or expired";
    public static final String JWT_VALIDATION_EXCEPTION_LOG = "Exception occurred while validating JWT Token: %s";
    public static final String JWT_MISSING_ERROR = "JWT Token is missing or invalid";

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
