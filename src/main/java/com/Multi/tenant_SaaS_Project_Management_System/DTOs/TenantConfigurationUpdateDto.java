package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TenantConfigurationUpdateDto {

    private String configValue;

    @NotNull(message = "Config type is required")
    private ConfigType configType;
}