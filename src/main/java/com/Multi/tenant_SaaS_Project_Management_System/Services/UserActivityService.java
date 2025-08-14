package com.Multi.tenant_SaaS_Project_Management_System.Services;


import com.Multi.tenant_SaaS_Project_Management_System.Entities.UserActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActivityService {

    List<UserActivity> getUserActivities(Long userId, LocalDateTime start, LocalDateTime end);

    List<UserActivity> getActivitiesByType(String activityType, LocalDateTime start, LocalDateTime end);

    UserActivity logActivity(UserActivity activity);
}
