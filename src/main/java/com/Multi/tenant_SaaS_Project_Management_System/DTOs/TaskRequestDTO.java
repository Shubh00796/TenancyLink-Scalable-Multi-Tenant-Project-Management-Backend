package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDTO {

    @NotBlank(message = "Task title is required")
    @Size(max = 255, message = "Task title cannot exceed 255 characters")
    private String title;

    private String description;

    @NotBlank(message = "Task code is required")
    @Size(max = 50, message = "Task code cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Task code must contain only uppercase letters, numbers, and hyphens")
    private String taskCode;

    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be positive")
    private Long projectId;

    @Positive(message = "Assignee user ID must be positive")
    private Long assigneeUserId;

    @NotNull(message = "Reporter user ID is required")
    @Positive(message = "Reporter user ID must be positive")
    private Long reporterUserId;

    private TaskStatus status;
    private TaskPriority priority;

    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated hours must be greater than 0")
    @Digits(integer = 6, fraction = 2, message = "Estimated hours must have at most 6 integer digits and 2 decimal places")
    private BigDecimal estimatedHours;

    @Column(name = "actual_hours", precision = 8, scale = 2) // adjust precision if needed
    private BigDecimal actualHours;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;
}