package com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices;


import com.Multi.tenant_SaaS_Project_Management_System.Entities.TenantConfiguration;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import com.Multi.tenant_SaaS_Project_Management_System.Repositories.TenantConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TenantConfigurationRepositoryService {

    private final TenantConfigurationRepository repository;

    public List<TenantConfiguration> getAllByTenantId(Long tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public TenantConfiguration getByTenantIdAndConfigKey(Long tenantId, String configKey) {
        return repository.findByTenantIdAndConfigKey(tenantId, configKey)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TenantConfiguration not found for tenantId " + tenantId + " and configKey " + configKey));
    }

    public List<TenantConfiguration> getByTenantIdAndConfigType(Long tenantId, ConfigType configType) {
        return repository.findByTenantIdAndConfigType(tenantId, configType);
    }

    public boolean existsByTenantIdAndConfigKey(Long tenantId, String configKey) {
        return repository.existsByTenantIdAndConfigKey(tenantId, configKey);
    }

    public TenantConfiguration create(TenantConfiguration tenantConfiguration) {
        Objects.requireNonNull(tenantConfiguration,"tenants can not be null");

        return repository.save(tenantConfiguration);
    }

    public TenantConfiguration update(TenantConfiguration tenantConfiguration) {
        Objects.requireNonNull(tenantConfiguration,"tenants can not be null");
        return repository.save(tenantConfiguration);
    }

    public void deleteByTenantIdAndConfigKey(Long tenantId, String configKey) {
        repository.deleteByTenantIdAndConfigKey(tenantId, configKey);
    }

    public TenantConfiguration getByTenantCodeAndConfigKey(String tenantCode, String configKey) {
        return repository.findByTenantCodeAndConfigKey(tenantCode, configKey)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TenantConfiguration not found for tenantCode " + tenantCode + " and configKey " + configKey));
    }

    public List<TenantConfiguration> getByTenantCode(String tenantCode) {
        return repository.findByTenantCode(tenantCode);
    }
}