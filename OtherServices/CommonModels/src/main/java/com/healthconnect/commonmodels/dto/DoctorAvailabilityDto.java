package com.healthconnect.commonmodels.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailabilityDto {

    private Long id;

    @NotBlank(message = "Day of the week cannot be blank")
    @Pattern(regexp = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)$",
            message = "Day of the week must be a valid day")
    private String dayOfWeek;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;
}
