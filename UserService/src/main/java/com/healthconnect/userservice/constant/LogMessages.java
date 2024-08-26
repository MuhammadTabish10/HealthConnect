package com.healthconnect.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogMessages {
    public static final String REGISTERING_USER_LOG = "Registering user with email: {}";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully.";
    public static final String USER_UPDATED_SUCCESSFULLY = "User updated successfully.";
    public static final String USER_CREATION_FAILED = "User creation failed.";
    public static final String USER_CREATED_SUCCESSFULLY = "User created successfully.";
    public static final String KEYCLOAK_USER_CREATION_ERROR = "Error creating user in Keycloak for email: {}";
    public static final String KEYCLOAK_USER_CREATION_FAILED = "Failed to create user in Keycloak. Response status: {}";
    public static final String USER_REGISTERED_SUCCESS_LOG = "User registered successfully with email: {}";
    public static final String FAILED_TO_CREATE_USER_IN_KEYCLOAK_LOG = "Failed to create user in Keycloak";
    public static final String USER_NOT_FOUND_IN_KEYCLOAK = "User not found in Keycloak";
    public static final String FAILED_TO_CREATE_USER_IN_KEYCLOAK_STATUS_LOG = "Failed to create user in Keycloak. Response status: ";


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
}
