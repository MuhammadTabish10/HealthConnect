package com.healthconnect.commonmodels.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {
    private Long id;

    private String notes;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date must be in the future or present")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    @NotNull(message = "Appointment status is required")
    private String status;

    private Boolean isActive;

    @NotNull(message = "User ID is required")
    private UserDto user;

    @NotNull(message = "Hospital ID is required")
    private HospitalDto hospital;

    @NotNull(message = "Doctor ID is required")
    private DoctorDto doctor;
}
