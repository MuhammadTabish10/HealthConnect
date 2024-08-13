package com.healthconnect.commonmodels.dto;

import com.healthconnect.commonmodels.model.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
    private Long id;

    @NotBlank(message = "Role name is mandatory")
    @Size(max = 50, message = "Role name must be less than 50 characters")
    private String name;

    private Boolean isActive;
    private Set<Permission> permissions;
}
