package com.healthconnect.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogMessages {

    public static final String REGISTERING_USER_LOG = "Registering user with email: %s";
    public static final String USER_REGISTERED_SUCCESS_LOG = "User with email: %s registered successfully";
    public static final String ROLE_NOT_FOUND_LOG = "Role %s not found for user registration";
    public static final String AUTHENTICATING_USER_LOG = "Attempting to authenticate user with email: %s";
    public static final String AUTHENTICATION_SUCCESS_LOG = "User with email: %s authenticated successfully, JWT token generated";


    public static final String LOG_MSG_USER_LOGGED_OUT_SUCCESS = "User logged out successfully with refresh token {}";
    public static final String LOG_MSG_UNEXPECTED_LOGOUT_RESPONSE = "Unexpected response during logout for refresh token {}: {}";
    public static final String LOG_MSG_LOGOUT_ERROR = "Error during logout for refresh token {}: {}";
    public static final String LOG_MSG_UNEXPECTED_LOGOUT_ERROR = "Unexpected error during logout for refresh token {}: {}";

    public static final String LOG_MSG_AUTHENTICATING_USER = "Authenticating user: {}";
    public static final String LOG_MSG_USER_AUTHENTICATION_FAILED = "Error during user authentication for {}: {}";
    public static final String LOG_MSG_FAILED_TO_RETRIEVE_TOKEN = "Failed to retrieve token from response for user {}";
    public static final String LOG_MSG_FAILED_TO_RETRIEVE_TOKEN_FROM_REFRESH_TOKEN = "Failed to retrieve token using refresh token {}";
    public static final String LOG_MSG_ERROR_GENERATING_ACCESS_TOKEN = "Error generating access token using refresh token {}: {}";

    public static final String ERROR_MSG_FAILED_TO_RETRIEVE_TOKEN = "Failed to retrieve token from response";
    public static final String ERROR_MSG_AUTHENTICATION_FAILED = "Authentication failed, please check your credentials and try again.";
    public static final String ERROR_MSG_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
    public static final String ERROR_MSG_UNEXPECTED_RESPONSE = "Unexpected response: ";



    public static final String LOADING_USER_BY_EMAIL_LOG = "Loading user by email: %s";
    public static final String USER_NOT_FOUND_LOG = "User not found with email: %s";
    public static final String USER_FOUND_LOG = "User found with email: %s";

    public static final String EXTRACTED_USERNAME_LOG = "Extracted username from token: %s";
    public static final String EXTRACTED_CLAIM_LOG = "Extracted claim from token: %s";
    public static final String GENERATING_TOKEN_LOG = "Generating JWT token for user: %s";
    public static final String GENERATED_TOKEN_LOG = "Generated JWT token for user: %s";
    public static final String TOKEN_VALIDATION_LOG = "JWT token validation for user %s: %s";
    public static final String BUILT_CLAIMS_LOG = "Built claims for user %s: %s";
    public static final String BUILT_TOKEN_LOG = "Built JWT token for subject %s: %s";
    public static final String CHECKED_TOKEN_EXPIRATION_LOG = "Checked token expiration: %s";
    public static final String EXTRACTED_EXPIRATION_LOG = "Extracted expiration date from token: %s";
    public static final String EXTRACTED_ALL_CLAIMS_LOG = "Extracted all claims from token: %s";
    public static final String GENERATED_SIGNING_KEY_LOG = "Generated signing key from secret";

}
