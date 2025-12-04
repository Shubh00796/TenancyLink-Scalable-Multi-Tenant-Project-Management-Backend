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

    // 2. Group tenants by plan and return tenant names as comma-separated
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

    // 10. Top N tenants by max users
    public List<TenantDto> topNTenantsByMaxUsers(List<TenantDto> tenants, int n) {
        return tenants.stream()
                .sorted(Comparator.comparingInt(TenantDto::getMaxUsers).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // 11. Group tenants by status and plan
    public List<TenantDto> groupByStatusAndPlan(List<TenantDto> tenantDtos, TenantStatus status, SubscriptionPlan plan) {
        return tenantDtos.stream()
                .filter(t -> t.getStatus() == status && t.getSubscriptionPlan() == plan)
                .collect(Collectors.toList());


    }

    // 12. Average max projects per subscription plan
    public Map<SubscriptionPlan, Double> averageMaxProjectsPerPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.groupingBy(
                        TenantDto::getSubscriptionPlan,
                        Collectors.averagingInt(TenantDto::getMaxProjects)
                ));
    }

    // 13. Count of tenants created in the last N days
    public long countTenantsCreatedInLastNDays(List<TenantDto> tenants, int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return tenants.stream()
                .filter(t -> t.getCreatedAt() != null && t.getCreatedAt().toLocalDate().isAfter(cutoffDate))
                .count();
    }


    // 14. Map of tenant codes to their statuses
    public Map<String, TenantStatus> mapTenantCodesToStatuses(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.toMap(
                        TenantDto::getTenantCode,
                        TenantDto::getStatus
                ));
    }

    // 15. List of tenants with more than X max users
    public List<TenantDto> tenantsWithMoreThanXMaxUsers(List<TenantDto> tenants, int x) {
        return tenants.stream()
                .filter(t -> t.getMaxUsers() > x)
                .collect(Collectors.toList());
    }

    // 16. Summary statistics of max projects
    public IntSummaryStatistics summaryStatisticsOfMaxProjects(List<TenantDto> tenants) {
        return tenants.stream()
                .collect(Collectors.summarizingInt(TenantDto::getMaxProjects));
    }


    // 17. Check if any tenant is on PREMIUM plan
    public boolean anyTenantOnPremiumPlan(List<TenantDto> tenants) {
        return tenants.stream()
                .anyMatch(t -> t.getSubscriptionPlan() == SubscriptionPlan.PREMIUM);
    }

    // 18. Get a set of all subscription plans in use
    public Set<SubscriptionPlan> allSubscriptionPlansInUse(List<TenantDto> tenants) {
        return tenants.stream()
                .map(TenantDto::getSubscriptionPlan)
                .collect(Collectors.toSet());
    }

    // 19. Find tenants with names starting with a specific prefix
    public List<TenantDto> tenantsWithNameStartingWith(List<TenantDto> tenants, String prefix) {
        return tenants.stream()
                .filter(t -> t.getTenantName() != null && t.getTenantName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    // 20. Calculate the total max users for active tenants
    public int totalMaxUsersForActiveTenants(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getStatus() == TenantStatus.ACTIVE)
                .mapToInt(TenantDto::getMaxUsers)
                .sum();

    }

    // 21. Find the tenant with the longest name
    public Optional<TenantDto> tenantWithLongestName(List<TenantDto> tenants) {
        return tenants.stream()
                .max(Comparator.comparingInt(t -> t.getTenantName() != null ? t.getTenantName().length() : 0));
    }

    // 22. Group tenants by the year they were created
    public Map<Integer, List<TenantDto>> groupTenantsByCreationYear(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> t.getCreatedAt() != null)
                .collect(Collectors.groupingBy(t -> t.getCreatedAt().getYear()));
    }

    // 23. Calculate the percentage of active tenants
    public double percentageOfActiveTenants(List<TenantDto> tenants) {
        long activeCount = tenants.stream()
                .filter(t -> t.getStatus() == TenantStatus.ACTIVE)
                .count();
        return tenants.isEmpty() ? 0.0 : (activeCount * 100.0) / tenants.size();
    }

    // 24. Find tenants created on weekends
    public List<TenantDto> tenantsCreatedOnWeekends(List<TenantDto> tenants) {
        return tenants.stream()
                .filter(t -> {
                    if (t.getCreatedAt() == null) return false;
                    int dayOfWeek = t.getCreatedAt().getDayOfWeek().getValue();
                    return dayOfWeek == 6 || dayOfWeek == 7; // Saturday or Sunday
                })
                .collect(Collectors.toList());
    }


}
