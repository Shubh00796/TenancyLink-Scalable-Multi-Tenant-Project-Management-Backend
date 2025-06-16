package com.Multi.tenant_SaaS_Project_Management_System.Mappers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.TenantConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TenantConfigurationMapper {

    TenantConfigurationDto toDto(TenantConfiguration tenantConfiguration);

    TenantConfiguration toEntity(TenantConfigurationDto tenantConfigurationDto);

    TenantConfiguration toEntity(TenantConfigurationCreateDto tenantConfigurationCreateDto);

    void updateEntityFromDto(TenantConfigurationUpdateDto tenantConfigurationUpdateDto, @MappingTarget TenantConfiguration tenantConfiguration);
}