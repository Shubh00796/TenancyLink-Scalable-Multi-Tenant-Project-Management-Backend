package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.Tenant;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Mappers.TenantMapper;
import com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices.TenantRepositoryService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TenantServiceImpl implements TenantService {
    private final TenantRepositoryService repositoryService;
    private final TenantMapper mapper;

    @Override
    @Transactional
    public TenantDto createTenant(TenantCreateDto createDto) {
        validateDto(createDto);
        Tenant entity = mapper.toEntity(createDto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setStatus(TenantStatus.ACTIVE);

        return mapper.toDto(repositoryService.create(entity));
    }

    private static void validateDto(TenantCreateDto createDto) {
        Objects.requireNonNull(createDto.getTenantName(), "name can not be null");
        Objects.requireNonNull(createDto.getTenantCode(), "code can not be null");
        Objects.requireNonNull(createDto.getDatabaseName(), "databasename can not be null");
    }

    @Override
    public TenantDto getTenantById(Long id) {
        Tenant tenantById = repositoryService.getByTenantById(id);
        return mapper.toDto(tenantById);
    }

    @Override
    public TenantDto getTenantByCode(String tenantCode) {
        Tenant byTenantCode = repositoryService.getByTenantCode(tenantCode);
        return mapper.toDto(byTenantCode);
    }

    @Override
    public TenantDto getTenantByIdWithConfigurations(Long id) {
        Tenant tenant = repositoryService.getByTenantById(id); // Replace if a method with eager fetch exists
        return mapper.toDto(tenant);
    }

    @Override
    public List<TenantDto> getAllTenants() {
        List<Tenant> tenants = repositoryService.getAllTenants(); // You need to add this method in repo service
        return tenants.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TenantDto> getAllTenantsByPage(Pageable pageable) {
        Page<Tenant> page = (Page<Tenant>) repositoryService.getAllTenantsByPage(pageable); // Add this method in repo service
        return page.map(mapper::toDto);
    }

    @Override
    public List<TenantDto> getTenantsByStatus(TenantStatus status) {
        List<Tenant> tenants = repositoryService.getByStatus(status);
        return tenants.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TenantDto updateTenant(Long id, TenantUpdateDto updateDto) {
        Tenant existingTenant = repositoryService.getByTenantById(id);
        Optional.ofNullable(existingTenant).orElseThrow(() -> new ResolutionException("You cant update Tenant if id is not present  + id"));
        mapper.updateEntityFromDto(updateDto, existingTenant);
        existingTenant.setUpdatedAt(LocalDateTime.now());
        Tenant updatedTenant = repositoryService.update(existingTenant);
        return mapper.toDto(updatedTenant);
    }

    @Override
    @Transactional
    public void deleteTenant(Long id) {
        repositoryService.deleteById(id);
    }

    @Override
    @Transactional
    public void deactivateTenant(Long id) {
        Tenant tenant = repositoryService.getByTenantById(id);
        tenant.setStatus(TenantStatus.INACTIVE);
        tenant.setUpdatedAt(LocalDateTime.now());
        repositoryService.update(tenant);
    }

    @Override
    @Transactional
    public void activateTenant(Long id) {
        Tenant tenant = repositoryService.getByTenantById(id);
        tenant.setStatus(TenantStatus.ACTIVE);
        tenant.setUpdatedAt(LocalDateTime.now());
        repositoryService.update(tenant);
    }

    @Override
    @Transactional
    public void suspendTenant(Long id) {
        Tenant tenant = repositoryService.getByTenantById(id);
        tenant.setStatus(TenantStatus.SUSPENDED);
        tenant.setUpdatedAt(LocalDateTime.now());
        repositoryService.update(tenant);
    }

    @Override
    public boolean existsByTenantCode(String tenantCode) {
        return repositoryService.existsByTenantCode(tenantCode);
    }

    @Override
    public boolean existsByDatabaseName(String databaseName) {
        return repositoryService.existsByDatabaseName(databaseName);
    }

    @Override
    public long countTenantsByStatus(TenantStatus status) {
        return repositoryService.countByStatus(status);
    }

    @Override
    public boolean isTenantActive(String tenantCode) {
        try {
            Tenant tenant = repositoryService.getActiveTenantByCode(tenantCode, TenantStatus.ACTIVE);
            return tenant != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canTenantAddUsers(Long tenantId, int currentUserCount) {
        Tenant tenant = repositoryService.getByTenantById(tenantId);
        // Example business rule: maxUsers is stored in tenant entity or config
        Integer maxUsers = tenant.getMaxUsers();
        if (maxUsers == null) {
            // No limit
            return true;
        }
        return currentUserCount < maxUsers;
    }

    @Override
    public boolean canTenantAddProjects(Long tenantId, int currentProjectCount) {
        Tenant tenant = repositoryService.getByTenantById(tenantId);
        // Example business rule: maxProjects is stored in tenant entity or config
        Integer maxProjects = tenant.getMaxProjects();
        if (maxProjects == null) {
            // No limit
            return true;
        }
        return currentProjectCount < maxProjects;
    }
}
