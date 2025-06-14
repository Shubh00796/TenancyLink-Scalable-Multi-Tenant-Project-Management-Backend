package com.Multi.tenant_SaaS_Project_Management_System.Services;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TenantService {

    TenantDto createTenant(TenantCreateDto createDto);

    TenantDto getTenantById(Long id);

    TenantDto getTenantByCode(String tenantCode);

    /**
     * Fetch tenant with configurations eagerly loaded if needed
     */

    List<TenantDto> getAllTenants();

    List<TenantDto> getTenantsByStatus(TenantStatus status);

    Page<TenantDto> getAllTenantsByPage(Pageable pageable);

    TenantDto updateTenant(Long id, TenantUpdateDto updateDto);

    void deleteTenant(Long id);

    void deactivateTenant(Long id);

    void activateTenant(Long id);

    void suspendTenant(Long id);

    boolean existsByTenantCode(String tenantCode);

    boolean existsByDatabaseName(String databaseName);

    long countTenantsByStatus(TenantStatus status);

    /**
     * Checks if tenant with tenantCode is active (status == ACTIVE)
     */
    boolean isTenantActive(String tenantCode);

    /**
     * Check if tenant can add users based on limits or business rules
     */
    boolean canTenantAddUsers(Long tenantId, int currentUserCount);

    /**
     * Check if tenant can add projects based on limits or business rules
     */
    boolean canTenantAddProjects(Long tenantId, int currentProjectCount);
}