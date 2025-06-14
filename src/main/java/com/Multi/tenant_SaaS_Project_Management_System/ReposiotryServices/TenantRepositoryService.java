package com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.Tenant;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import com.Multi.tenant_SaaS_Project_Management_System.Repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TenantRepositoryService {

    private final TenantRepository repository;

    public Tenant getByTenantCode(String tenantCode) {
        return repository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with tenantCode " + tenantCode));
    }

    public Tenant getByTenantById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with tenantCode " + id));
    }

    public Tenant getByDatabaseName(String databaseName) {
        return repository.findByDatabaseName(databaseName)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with databaseName " + databaseName));
    }


    public List<Tenant> getByStatus(TenantStatus status) {
        return repository.findByStatus(status);
    }


    public List<Tenant> getAllTenants() {
        return repository.findAll();
    }

    public List<Tenant> getAllTenantsByPage(Pageable pageable) {
        return repository.findAll();
    }

    public boolean existsByTenantCode(String tenantCode) {
        return repository.existsByTenantCode(tenantCode);
    }

    public boolean existsByDatabaseName(String databaseName) {
        return repository.existsByDatabaseName(databaseName);
    }

    public Tenant getActiveTenantByCode(String tenantCode, TenantStatus status) {
        return repository.findActiveTenantByCode(tenantCode, status)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Active tenant not found with tenantCode " + tenantCode + " and status " + status));
    }

    public long countByStatus(TenantStatus status) {
        return repository.countByStatus(status);
    }

    public Tenant create(Tenant tenant) {
        Objects.requireNonNull(tenant, "tenants can not be null");
        return repository.save(tenant);
    }

    public Tenant update(Tenant tenant) {
        Objects.requireNonNull(tenant, "tenants can not be null");
        return repository.save(tenant);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}