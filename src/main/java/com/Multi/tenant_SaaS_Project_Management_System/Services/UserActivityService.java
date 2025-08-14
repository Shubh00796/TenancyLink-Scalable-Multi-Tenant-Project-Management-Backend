package com.Multi.tenant_SaaS_Project_Management_System.Services;


import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserActivityRequest;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserActivityResponse;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.UserActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActivityService {

    List<UserActivityResponse> getUserActivities(Long userId, LocalDateTime start, LocalDateTime end);

    List<UserActivityResponse> getActivitiesByType(String activityType, LocalDateTime start, LocalDateTime end);

    UserActivityResponse logActivity(UserActivityRequest request);
}
