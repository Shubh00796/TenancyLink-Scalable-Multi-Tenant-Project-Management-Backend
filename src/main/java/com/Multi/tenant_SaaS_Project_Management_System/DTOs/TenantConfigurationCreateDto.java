package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TenantConfigurationCreateDto {

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotBlank(message = "Config key is required")
    private String configKey;

    private String configValue;

    @NotNull(message = "Config type is required")
    private ConfigType configType;
}