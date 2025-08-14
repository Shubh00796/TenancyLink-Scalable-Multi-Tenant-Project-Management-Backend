package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;


import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserActivityRequest;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserActivityResponse;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.UserActivity;
import com.Multi.tenant_SaaS_Project_Management_System.Mappers.UserActivityMapper;
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
    private final UserActivityMapper mapper;

    @Override
    public List<UserActivityResponse> getUserActivities(Long userId, LocalDateTime start, LocalDateTime end) {
        return Optional.ofNullable(repository.findByUserIdAndActivityTimeBetween(userId, start, end))
                .orElseGet(List::of)
                .stream()
                .sorted(Comparator.comparing(UserActivity::getActivityTime).reversed())
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserActivityResponse> getActivitiesByType(String activityType, LocalDateTime start, LocalDateTime end) {
        return Optional.ofNullable(repository.findByActivityTypeAndActivityTimeBetween(activityType, start, end))
                .orElseGet(List::of)
                .stream()
                .filter(activity -> Optional.ofNullable(activity.getDescription())
                        .map(desc -> !desc.isBlank())
                        .orElse(false))
                .sorted(Comparator.comparing(UserActivity::getActivityTime))
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserActivityResponse logActivity(UserActivityRequest request) {
        UserActivity savedEntity = Optional.ofNullable(request)
                .map(mapper::toEntity)
                .map(repository::save)
                .orElseThrow(() -> new IllegalArgumentException("Activity cannot be null"));
        return mapper.toResponse(savedEntity);
    }
}



