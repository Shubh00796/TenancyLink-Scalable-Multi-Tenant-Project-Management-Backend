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
public class TenantLifecycleInsightsMayService {

    /**
     * Partitions tenants into active vs inactive.
     */
    public Map<Boolean, List<TenantDto>> partitionActiveVsInactive(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.partitioningBy(t -> t.getStatus() == TenantStatus.ACTIVE));
    }

    /**
     * Groups tenants by subscription plan and returns tenant names as comma-separated strings.
     */
    public Map<SubscriptionPlan, String> tenantNamesPerPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.groupingBy(
                        TenantDto::getSubscriptionPlan,
                        Collectors.mapping(TenantDto::getTenantName, Collectors.joining(", "))
                ));
    }

    /**
     * Counts churned (inactive) tenants per subscription plan.
     */
    public Map<SubscriptionPlan, Long> churnedTenantCountPerPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getStatus() == TenantStatus.INACTIVE)
                .collect(Collectors.groupingBy(TenantDto::getSubscriptionPlan, Collectors.counting()));
    }

    /**
     * Calculates average lifespan (in days) of tenants before churn.
     */
    public double averageLifespanBeforeChurn(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getStatus() == TenantStatus.INACTIVE &&
                        t.getCreatedAt() != null &&
                        t.getUpdatedAt() != null)
                .mapToInt(t -> Period.between(
                        t.getCreatedAt().toLocalDate(),
                        t.getUpdatedAt().toLocalDate()
                ).getDays())
                .average()
                .orElse(0.0);
    }

    /**
     * Detects tenants who downgraded to BASIC plan.
     * Assumes downgrade is reflected by updatedAt being after createdAt.
     */
    public List<TenantDto> detectDowngradedTenants(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getCreatedAt() != null &&
                        t.getUpdatedAt() != null &&
                        t.getSubscriptionPlan() == SubscriptionPlan.BASIC)
                .collect(Collectors.toList());
    }

    /**
     * Finds the earliest tenant creation date.
     */
    public Optional<LocalDate> earliestTenantCreation(List<TenantDto> tenants) {
        return tenants.stream()
                .map(t -> t.getCreatedAt().toLocalDate())
                .min(LocalDate::compareTo);
    }

    /**
     * Finds the latest tenant creation date.
     */
    public Optional<LocalDate> latestTenantCreation(List<TenantDto> tenants) {
        return tenants.stream()
                .map(t -> t.getCreatedAt().toLocalDate())
                .max(LocalDate::compareTo);
    }

    /**
     * Extracts unique tenant codes.
     */
    public List<String> uniqueTenantCodes(List<TenantDto> tenants) {
        return tenants.stream()
                .map(TenantDto::getTenantCode)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Calculates total projects across all tenants.
     */
    public int totalProjects(List<TenantDto> tenants) {
        return tenants.stream()
                .mapToInt(TenantDto::getMaxProjects)
                .sum();
    }

    /**
     * Tracks tenant creation trend by month.
     */
    public Map<Integer, Long> tenantCreationTrendByMonth(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCreatedAt().getMonthValue(),
                        Collectors.counting()
                ));
    }
}
