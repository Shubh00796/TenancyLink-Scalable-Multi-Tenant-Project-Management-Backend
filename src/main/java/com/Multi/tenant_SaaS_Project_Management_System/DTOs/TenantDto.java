package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.SubscriptionPlan;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TenantDto {
    private Long id;
    private String tenantCode;
    private String tenantName;
    private String databaseName;
    private TenantStatus status;
    private SubscriptionPlan subscriptionPlan;
    private Integer maxUsers;
    private Integer maxProjects;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}