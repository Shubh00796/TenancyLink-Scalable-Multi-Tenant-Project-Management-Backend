package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;


import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.SubscriptionPlan;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TenantUsageAnalyticsService {

    // 1. Find tenants exceeding max user limit
    public List<TenantDto> findOverLimitUsers(List<TenantDto> tenants, Map<Long, Integer> actualUserCounts) {
        return tenants.stream()
                .filter(t -> actualUserCounts.getOrDefault(t.getId(), 0) > t.getMaxUsers())
                .collect(Collectors.toList());
    }

    // 2. Find tenants exceeding max project limit
    public List<TenantDto> findOverLimitProjects(List<TenantDto> tenants, Map<Long, Integer> actualProjectCounts) {
        return tenants.stream()
                .filter(t -> actualProjectCounts.getOrDefault(t.getId(), 0) > t.getMaxProjects())
                .collect(Collectors.toList());
    }

    // 3. Summarize active tenant counts per subscription plan
    public Map<SubscriptionPlan, Long> activeTenantCountPerPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getStatus() == TenantStatus.ACTIVE)
                .collect(Collectors.groupingBy(TenantDto::getSubscriptionPlan, Collectors.counting()));
    }

    // 4. Find top N tenants by actual user count
    public List<TenantDto> topTenantsByUsers(List<TenantDto> tenants, Map<Long, Integer> actualUserCounts, int limit) {
        return tenants.stream()
                .sorted(Comparator.comparing((TenantDto t) -> actualUserCounts.getOrDefault(t.getId(), 0)).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 5. Map tenantId -> usage percentage (users)
    public Map<Long, Double> userUsagePercentage(List<TenantDto> tenants, Map<Long, Integer> actualUserCounts) {
        return tenants.stream()
                .collect(Collectors.toMap(
                        TenantDto::getId,
                        t -> {
                            int actual = actualUserCounts.getOrDefault(t.getId(), 0);
                            return t.getMaxUsers() == 0 ? 0.0 : (actual * 100.0) / t.getMaxUsers();
                        }
                ));
    }

    // 6. Get tenant with highest resource usage (projects)
    public Optional<TenantDto> tenantWithHighestProjectUsage(List<TenantDto> tenants, Map<Long, Integer> actualProjectCounts) {
        return tenants.stream()
                .max(Comparator.comparing(t -> actualProjectCounts.getOrDefault(t.getId(), 0)));
    }

    // 7. Create subscription plan -> average user limit
    public Map<SubscriptionPlan, Double> averageUserLimitPerPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.groupingBy(
                        TenantDto::getSubscriptionPlan,
                        Collectors.averagingInt(TenantDto::getMaxUsers)
                ));
    }

    // 8. Reusable generic method: convert List<T> to Map<K, V> safely
    public <T, K, V> Map<K, V> toMap(List<T> items, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return items.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v1)); // merge by keeping first
    }
}
