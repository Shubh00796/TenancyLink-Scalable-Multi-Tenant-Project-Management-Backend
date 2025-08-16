package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.SubscriptionPlan;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantAnalyticsService {
    // 1. Filter tenants by status
    public List<TenantDto> filterByStatus(List<TenantDto> tenants, TenantStatus status) {
        return tenants.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    // 2. Sort tenants by creation date (newest first)
    public List<TenantDto> sortByCreationDate(List<TenantDto> tenants) {
        return tenants.stream()
                .sorted(Comparator.comparing(TenantDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // 3. Group tenants by subscription plan
    public Map<SubscriptionPlan, List<TenantDto>> groupBySubscriptionPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.groupingBy(TenantDto::getSubscriptionPlan));
    }

    // 4. Count tenants per status
    public Map<TenantStatus, Long> countTenantsPerStatus(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.groupingBy(TenantDto::getStatus, Collectors.counting()));
    }

    // 5. Get tenant with max allowed projects using Optional
    public Optional<TenantDto> getTenantWithMaxProjects(List<TenantDto> tenants) {
        return tenants.stream()
                .max(Comparator.comparing(TenantDto::getMaxProjects));
    }

    // 6. Calculate total allowed users for active tenants
    public int totalAllowedUsersForActive(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(tenantDto -> tenantDto.getStatus() == TenantStatus.ACTIVE)
                .mapToInt(TenantDto::getMaxUsers)
                .sum();

    }
    // 7. Find tenants created in a given date range
    public List<TenantDto> filterByDateRange(List<TenantDto> tenants, LocalDateTime start, LocalDateTime end) {
        return tenants.stream()
                .filter(t -> !t.getCreatedAt().isBefore(start) && !t.getCreatedAt().isAfter(end))
                .collect(Collectors.toList());
    }

    // 8. Map subscription plan to total allowed projects
    public Map<SubscriptionPlan, Integer> totalProjectsPerPlan(List<TenantDto> tenants) {
        return getCollect(tenants);
    }

    private static Map<SubscriptionPlan, Integer> getCollect(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.groupingBy(TenantDto::getSubscriptionPlan,
                        Collectors.summingInt(TenantDto::getMaxProjects)));
    }


}
