package com.Multi.tenant_SaaS_Project_Management_System.Services;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;

import java.util.List;
import java.util.Optional;

public interface TenantConfigurationService {

    TenantConfigurationDto createConfiguration(TenantConfigurationCreateDto createDto);

    Optional<TenantConfigurationDto> getConfigurationById(Long id);

    Optional<TenantConfigurationDto> getConfigurationByTenantAndKey(Long tenantId, String configKey);

    Optional<TenantConfigurationDto> getConfigurationByTenantCodeAndKey(String tenantCode, String configKey);

    List<TenantConfigurationDto> getConfigurationsByTenantId(Long tenantId);

    List<TenantConfigurationDto> getConfigurationsByTenantCode(String tenantCode);

    List<TenantConfigurationDto> getConfigurationsByTenantIdAndType(Long tenantId, ConfigType configType);


    /**
     * Create or update configuration atomically
     */

    void deleteConfiguration(Long id);

    void deleteConfigurationByTenantAndKey(Long tenantId, String configKey);

    boolean existsByTenantIdAndConfigKey(Long tenantId, String configKey);

    /**
     * Returns raw configuration value, null if not found
     */
    Object getConfigurationValue(Long tenantId, String configKey);

    /**
     * Returns string value or default if not found
     */
    String getConfigurationValueAsString(Long tenantId, String configKey, String defaultValue);

    /**
     * Returns boolean value or default if not found
     */
    Boolean getConfigurationValueAsBoolean(Long tenantId, String configKey, Boolean defaultValue);

    /**
     * Returns integer value or default if not found
     */
    Integer getConfigurationValueAsInteger(Long tenantId, String configKey, Integer defaultValue);

    /**
     * Returns double value or default if not found
     */
    Double getConfigurationValueAsDouble(Long tenantId, String configKey, Double defaultValue);
}