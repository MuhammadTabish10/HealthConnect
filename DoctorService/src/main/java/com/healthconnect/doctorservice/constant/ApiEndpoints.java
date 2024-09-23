package com.healthconnect.doctorservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {
    public static final String BASE_API = "/api/v1";
    public static final String DOCTORS = BASE_API + "/doctors";
    public static final String DOCTORS_AVAILABLE = "/availability";
    public static final String DOCTORS_BY_IDS = "/ids/{doctor-ids}";
    public static final String DOCTORS_AVAILABILITY_BY_DOCTOR_ID = "/availability/{doctor-id}";
}
