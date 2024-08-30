package com.healthconnect.commonmodels.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDto {
    private Long id;

    @NotBlank(message = "City name is required")
    @Size(max = 50, message = "City name cannot exceed 50 characters")
    private String name;

    private Boolean isActive;
}
