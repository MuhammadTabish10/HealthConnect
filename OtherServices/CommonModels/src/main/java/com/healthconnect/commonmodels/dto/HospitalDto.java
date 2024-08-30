package com.healthconnect.commonmodels.dto;

import com.healthconnect.commonmodels.model.hospital.City;
import com.healthconnect.commonmodels.model.hospital.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDto {

    private Long id;

    @NotBlank(message = "Hospital name is required")
    @Size(max = 100, message = "Hospital name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Hospital address is required")
    @Size(max = 255, message = "Hospital address cannot exceed 255 characters")
    private String address;

    @NotNull(message = "City is required")
    private City city;

    private Location location;
    private Boolean isActive;
}
