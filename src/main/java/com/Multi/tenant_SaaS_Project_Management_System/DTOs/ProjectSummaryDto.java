package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectSummaryDto {
    private Long id;
    private String name;
    private String projectCode;
    private ProjectStatus status;
    private ProjectPriority priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long ownerUserId;
    private boolean overdue;
}