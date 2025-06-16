package com.Multi.tenant_SaaS_Project_Management_System.DTOs;


import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private String projectCode;
    private ProjectStatus status;
    private ProjectPriority priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long ownerUserId;
    private Long createdByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
    private boolean completed;
    private boolean overdue;
    private long durationInDays;
}