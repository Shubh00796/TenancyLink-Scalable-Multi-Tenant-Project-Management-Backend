package com.Multi.tenant_SaaS_Project_Management_System.Repositories;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.TenantConfiguration;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantConfigurationRepository extends JpaRepository<TenantConfiguration, Long> {
    List<TenantConfiguration> findByTenantId(Long tenantId);

    Optional<TenantConfiguration> findByTenantIdAndConfigKey(Long tenantId, String configKey);

    List<TenantConfiguration> findByTenantIdAndConfigType(Long tenantId, ConfigType configType);

    boolean existsByTenantIdAndConfigKey(Long tenantId, String configKey);

    void deleteByTenantIdAndConfigKey(Long tenantId, String configKey);

    @Query(value = "SELECT tc.* FROM tenant_configurations tc " +
            "JOIN tenants t ON tc.tenant_id = t.id " +
            "WHERE t.tenant_code = :tenantCode AND tc.config_key = :configKey",
            nativeQuery = true)
    Optional<TenantConfiguration> findByTenantCodeAndConfigKey(@Param("tenantCode") String tenantCode,
                                                               @Param("configKey") String configKey);

    @Query(value = "SELECT tc.* FROM tenant_configurations tc " +
            "JOIN tenants t ON tc.tenant_id = t.id " +
            "WHERE t.tenant_code = :tenantCode",
            nativeQuery = true)
    List<TenantConfiguration> findByTenantCode(@Param("tenantCode") String tenantCode);
}
