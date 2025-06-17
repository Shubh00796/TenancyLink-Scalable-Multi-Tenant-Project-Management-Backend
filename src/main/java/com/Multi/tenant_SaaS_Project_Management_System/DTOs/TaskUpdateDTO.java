package com.Multi.tenant_SaaS_Project_Management_System.DTOs;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateDTO {

    @Size(max = 255, message = "Task title cannot exceed 255 characters")
    private String title;

    private String description;

    @Positive(message = "Assignee user ID must be positive")
    private Long assigneeUserId;

    private TaskStatus status;
    private TaskPriority priority;

    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated hours must be greater than 0")
    @Digits(integer = 6, fraction = 2, message = "Estimated hours must have at most 6 integer digits and 2 decimal places")
    private BigDecimal estimatedHours;

    @DecimalMin(value = "0.0", message = "Actual hours cannot be negative")
    @Digits(integer = 6, fraction = 2, message = "Actual hours must have at most 6 integer digits and 2 decimal places")
    private BigDecimal actualHours;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
}