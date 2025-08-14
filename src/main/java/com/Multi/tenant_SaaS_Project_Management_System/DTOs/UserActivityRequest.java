package com.Multi.tenant_SaaS_Project_Management_System.DTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActivityRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Activity type cannot be empty")
    private String activityType;

    private String description;

    @NotNull(message = "Activity time is required")
    private LocalDateTime activityTime;
}
