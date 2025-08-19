package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;


import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.SubscriptionPlan;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TenantLifecycleInsightsService {

    // 1. Partition tenants into active vs inactive
    public Map<Boolean, List<TenantDto>> partitionActiveVsInactive(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.partitioningBy(t -> t.getStatus() == TenantStatus.ACTIVE));
    }

    // 2. Group tenants by plan and return tenant names as comma-separated string
    public Map<SubscriptionPlan, String> tenantNamesPerPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.groupingBy(
                        TenantDto::getSubscriptionPlan,
                        Collectors.mapping(TenantDto::getTenantName, Collectors.joining(", "))
                ));
    }

    // 3. Count churned tenants (INACTIVE) per plan
    public Map<SubscriptionPlan, Long> churnedTenantCountPerPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getStatus() == TenantStatus.INACTIVE)
                .collect(Collectors.groupingBy(TenantDto::getSubscriptionPlan, Collectors.counting()));
    }

    // 4. Average lifespan (in days) of tenants before churn
    public double averageLifespanBeforeChurn(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getStatus() == TenantStatus.INACTIVE && t.getCreatedAt() != null && t.getUpdatedAt() != null)
                .mapToInt(t -> Period.between(t.getCreatedAt().toLocalDate(), t.getUpdatedAt().toLocalDate()).getDays())
                .average()
                .orElse(0.0);
    }

    // 5. Find all tenants who downgraded (higher plan -> lower plan)
    // Mock logic: assuming updatedAt means downgrade happened after createdAt
    public List<TenantDto> detectDowngradedTenants(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getCreatedAt() != null && t.getUpdatedAt() != null)
                .filter(t -> t.getSubscriptionPlan() == SubscriptionPlan.BASIC) // simplification
                .collect(Collectors.toList());
    }

    // 6. Earliest and latest tenant creation dates
    public Optional<LocalDate> earliestTenantCreation(List<TenantDto> tenants) {
        return tenants.stream()
                .map(t -> t.getCreatedAt().toLocalDate())
                .min(LocalDate::compareTo);
    }

    public Optional<LocalDate> latestTenantCreation(List<TenantDto> tenants) {
        return tenants.stream()
                .map(t -> t.getCreatedAt().toLocalDate())
                .max(LocalDate::compareTo);
    }

    // 7. Unique tenant codes (distinct + map)
    public List<String> uniqueTenantCodes(List<TenantDto> tenants) {
        return tenants.stream()
                .map(TenantDto::getTenantCode)
                .distinct()
                .collect(Collectors.toList());
    }

    // 8. Total projects across all tenants (reduce)
    public int totalProjects(List<TenantDto> tenants) {
        return tenants.stream()
                .map(TenantDto::getMaxProjects)
                .reduce(0, Integer::sum);
    }

    // 9. Tenants created per month (timeline trend)
    public Map<Integer, Long> tenantCreationTrendByMonth(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCreatedAt().getMonthValue(),
                        Collectors.counting()
                ));
    }
}
