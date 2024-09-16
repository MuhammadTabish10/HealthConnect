package com.healthconnect.commonmodels.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {

    private Long id;

    @NotBlank(message = "License number cannot be blank")
    @Size(min = 5, max = 20, message = "License number must be between 5 and 20 characters")
    private String licenseNumber;

    @NotBlank(message = "Specialty cannot be blank")
    @Size(min = 2, max = 50, message = "Specialty must be between 2 and 50 characters")
    private String specialty;

    @PositiveOrZero(message = "Years of experience cannot be negative")
    @Max(value = 100, message = "Years of experience cannot exceed 100")
    private Integer yearsOfExperience;

    @Size(max = 200, message = "Education field must not exceed 200 characters")
    private String education;

    @DecimalMin(value = "0.0", inclusive = false, message = "Consultation fee must be greater than 0")
    private Double consultationFee;

    @DecimalMin(value = "0.0", inclusive = true, message = "Ratings must be between 0 and 5")
    @DecimalMax(value = "5.0", inclusive = true, message = "Ratings must be between 0 and 5")
    private Double ratings;

    @Size(max = 2000, message = "Bio must not exceed 2000 characters")
    private String bio;

    @NotNull(message = "UserID cannot be null")
    private UserDto user;

    @NotEmpty(message = "Hospital IDs list cannot be empty")
    private List<HospitalDto> hospitals;

    @NotEmpty(message = "Availabilities list cannot be empty")
    private List<@Valid DoctorAvailabilityDto> availabilities;
    private Boolean isActive;
}
