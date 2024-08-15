package com.healthconnect.baseservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    public static final String ENTITY_SAVE_FAILED = "Failed to save %s";
    public static final String ENTITY_RETRIEVE_FAILED = "Failed to retrieve %s entities";
    public static final String ENTITY_NOT_FOUND_WITH_EMAIL = "%s with email %s not found";
    public static final String ENTITY_NOT_FOUND_AT_ID = "%s with ID %d not found";
    public static final String ENTITY_NOT_FOUND_BY_NAME = "%s with Name %s not found";
    public static final String ENTITY_UPDATE_FAILED = "Failed to update %s with ID %d";
    public static final String ENTITY_DELETE_FAILED = "Failed to deactivate %s with ID %d";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred: ";
}
