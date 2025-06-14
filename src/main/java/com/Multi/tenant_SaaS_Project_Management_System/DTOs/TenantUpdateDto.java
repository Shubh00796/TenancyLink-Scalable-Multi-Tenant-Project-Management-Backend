package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.SubscriptionPlan;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TenantUpdateDto {

    @NotBlank(message = "Tenant name is required")
    private String tenantName;

    @NotNull(message = "Status is required")
    private TenantStatus status;

    @NotNull(message = "Subscription plan is required")
    private SubscriptionPlan subscriptionPlan;

    @Positive(message = "Max users must be positive")
    private Integer maxUsers;

    @Positive(message = "Max projects must be positive")
    private Integer maxProjects;
}