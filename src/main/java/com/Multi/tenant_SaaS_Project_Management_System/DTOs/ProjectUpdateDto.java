package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectUpdateDto {

    @NotBlank(message = "Project name is required")
    @Size(max = 255, message = "Project name cannot exceed 255 characters")
    private String name;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @NotNull(message = "Status is required")
    private ProjectStatus status;

    @NotNull(message = "Priority is required")
    private ProjectPriority priority;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Owner user ID is required")
    private Long ownerUserId;
}