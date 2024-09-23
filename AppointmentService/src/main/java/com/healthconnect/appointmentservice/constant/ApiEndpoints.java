package com.healthconnect.appointmentservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {
    public static final String BASE_API = "/api/v1";
    public static final String APPOINTMENT = BASE_API + "/appointments";
    public static final String APPOINTMENTS_FILTER = "/filter";
    public static final String AVAILABLE_SLOTS = "/doctor/{doctorId}/available-slots";
    public static final String CANCEL = "/cancel/{id}";
}
