package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.UserActivity;
import com.Multi.tenant_SaaS_Project_Management_System.Repositories.UserActivityRepository;
import com.Multi.tenant_SaaS_Project_Management_System.Services.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {
    private final UserActivityRepository repository;

    @Override
    public List<UserActivity> getUserActivities(Long userId, LocalDateTime start, LocalDateTime end) {
        return Optional.ofNullable(repository.findByUserIdAndActivityTimeBetween(userId, start, end))
                .orElse(List.of())
                .stream()
                .sorted(Comparator.comparing(UserActivity::getActivityTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<UserActivity> getActivitiesByType(String activityType, LocalDateTime start, LocalDateTime end) {
        return Optional.ofNullable(repository.findByActivityTypeAndActivityTimeBetween(activityType, start, end))
                .orElse(List.of())
                .stream()
                .filter(userActivity -> userActivity.getDescription() != null && !userActivity.getDescription().isBlank())
                .sorted(Comparator.comparing(UserActivity::getActivityTime))
                .collect(Collectors.toList());
    }

    @Override
    public UserActivity logActivity(UserActivity activity) {
        return Optional.ofNullable(activity)
                .map(repository::save)
                .orElseThrow(() -> new IllegalArgumentException("Activity cannot be null"));
    }

}
