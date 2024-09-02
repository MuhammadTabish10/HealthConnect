package com.healthconnect.hospitalservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {
    public static final String FETCH_HOSPITAL_BY_LOCATION = "Fetching Hospital by latitude: {} and longitude: {}";
    public static final String HOSPITAL_BY_LOCATION_FOUND = "Hospital with latitude: {} and longitude: {} found";
    public static final String HOSPITAL_BY_LOCATION_NOT_FOUND = "Hospital with latitude: %f and longitude: %f not found";
}
