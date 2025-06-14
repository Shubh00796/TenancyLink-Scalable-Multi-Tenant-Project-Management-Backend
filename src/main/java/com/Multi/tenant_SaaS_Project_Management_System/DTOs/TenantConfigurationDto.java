package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TenantConfigurationDto {
    private Long id;
    private Long tenantId;
    private String configKey;
    private String configValue;
    private ConfigType configType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}