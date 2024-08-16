package com.healthconnect.baseservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogMessages {

    public static final String DIVIDER = "==========================================================";
    public static final String SECTION_DIVIDER = "--------------------------------------";
    public static final String ARROW = "➡️";
    public static final String CHECK_MARK = "✅";
    public static final String ERROR = "❌";
    public static final String REQUEST_RECEIVED_HEADER = "\n{}\n{} REQUEST RECEIVED {}\n{}";
    public static final String RESPONSE_SENT_HEADER = "\n{}\n{} RESPONSE SENT {}\n{}";
    public static final String METHOD_LABEL = "HTTP Method: {} {}";
    public static final String HEADERS_LABEL = "{} Headers {}";
    public static final String QUERY_PARAMS_LABEL = "{} Query Parameters {}";
    public static final String STATUS_CODE_LABEL = "HTTP Status Code: {} {}";
    public static final String REQUEST_URI_LABEL = "Request URI: {} {}";
    public static final String REQUEST_BODY_LABEL = "{} Request Body {}";
    public static final String RESPONSE_BODY_LABEL = "{} Response Body {}";

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
