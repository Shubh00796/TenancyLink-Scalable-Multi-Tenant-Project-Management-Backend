package com.Multi.tenant_SaaS_Project_Management_System.Entities;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.SubscriptionPlan;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenants", indexes = {
        @Index(name = "idx_tenant_code", columnList = "tenant_code"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_code", unique = true, nullable = false, length = 50)
    private String tenantCode;

    @Column(name = "tenant_name", nullable = false)
    private String tenantName;

    @Column(name = "database_name", nullable = false, length = 100)
    private String databaseName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private TenantStatus status = TenantStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan", nullable = false)
    @Builder.Default
    private SubscriptionPlan subscriptionPlan = SubscriptionPlan.BASIC;

    @Column(name = "max_users")
    @Builder.Default
    private Integer maxUsers = 10;

    @Column(name = "max_projects")
    @Builder.Default
    private Integer maxProjects = 5;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Business methods
    public boolean isActive() {
        return TenantStatus.ACTIVE.equals(this.status);
    }

    public boolean canAddUsers(int currentUserCount) {
        return currentUserCount < this.maxUsers;
    }

    public boolean canAddProjects(int currentProjectCount) {
        return currentProjectCount < this.maxProjects;
    }
}