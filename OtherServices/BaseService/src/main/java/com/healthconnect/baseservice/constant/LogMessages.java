package com.healthconnect.baseservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogMessages {
    public static final String ENTITY_SAVE_SUCCESS = "Entity of type {} saved successfully with ID: {}";
    public static final String ENTITY_FETCH_SUCCESS = "Entity of type {} found with ID: {}";
    public static final String ENTITY_FETCH_ALL_SUCCESS = "Fetched {} entities of type {}";
    public static final String ENTITY_UPDATE_SUCCESS = "Entity of type {} with ID: {} updated successfully";
    public static final String ENTITY_DELETE_SUCCESS = "Entity of type {} with ID: {} deleted successfully";

    public static final String ENTITY_SAVE_START = "Saving entity of type: {}";
    public static final String ENTITY_FETCH_START = "Fetching entity of type {} with ID: {}";
    public static final String ENTITY_FETCH_ALL_START = "Fetching all entities of type: {} with active status: {}";
    public static final String ENTITY_UPDATE_START = "Updating entity of type {} with ID: {}";
    public static final String ENTITY_DELETE_START = "Deleting entity of type {} with ID: {}";

    public static final String ENTITY_SAVE_ERROR = "Failed to save entity of type {}: {}";
    public static final String ENTITY_FETCH_ERROR = "Entity of type {} with ID: {} not found";
    public static final String ENTITY_FETCH_ALL_ERROR = "Failed to retrieve entities of type {}: {}";
    public static final String ENTITY_UPDATE_ERROR = "Failed to update entity of type {} with ID: {}: {}";
    public static final String ENTITY_DELETE_ERROR = "Failed to delete entity of type {} with ID: {}: {}";
}
