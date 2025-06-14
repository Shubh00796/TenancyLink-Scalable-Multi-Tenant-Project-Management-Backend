package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.SubscriptionPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
public class TenantCreateDto {

    @NotBlank(message = "Tenant code is required")
    private String tenantCode;

    @NotBlank(message = "Tenant name is required")
    private String tenantName;

    @NotBlank(message = "Database name is required")
    private String databaseName;

    @NotNull
    private SubscriptionPlan subscriptionPlan;

    @Positive(message = "Max users must be positive")
    private Integer maxUsers;

    @Positive(message = "Max projects must be positive")
    private Integer maxProjects;
}