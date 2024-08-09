package com.healthconnect.userservice.dto;

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
public class PermissionDto {
    private Long id;

    @NotBlank(message = "Permission name is mandatory")
    @Size(max = 50, message = "Permission name must be less than 50 characters")
    private String name;

    private Boolean isActive;
}
