package com.healthconnect.hospitalservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {
    public static final String BASE_API = "/api/v1";
    public static final String HOSPITALS = BASE_API + "/hospitals";
    public static final String HOSPITALS_BY_IDS = "/ids/{hospital-ids}";
    public static final String CITIES = BASE_API + "/cities";
    public static final String CITY = "/city";
    public static final String LOCATION = "/location";
}
