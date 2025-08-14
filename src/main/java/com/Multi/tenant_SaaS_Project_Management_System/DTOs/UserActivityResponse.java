package com.Multi.tenant_SaaS_Project_Management_System.DTOs;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActivityResponse {
    private Long id;
    private Long userId;
    private String activityType;
    private String description;
    private LocalDateTime activityTime;
}
