package com.Multi.tenant_SaaS_Project_Management_System.Repositories;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.Tenant;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByTenantCode(String tenantCode);

    Optional<Tenant> findByDatabaseName(String databaseName);



    List<Tenant> findByStatus(TenantStatus status);

    boolean existsByTenantCode(String tenantCode);

    boolean existsByDatabaseName(String databaseName);

    @Query("SELECT t FROM Tenant t WHERE t.tenantCode = :tenantCode AND t.status = :status")
    Optional<Tenant> findActiveTenantByCode(@Param("tenantCode") String tenantCode, @Param("status") TenantStatus status);

    @Query("SELECT COUNT(t) FROM Tenant t WHERE t.status = :status")
    long countByStatus(@Param("status") TenantStatus status);

}