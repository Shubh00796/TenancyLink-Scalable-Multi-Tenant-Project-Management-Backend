package com.Multi.tenant_SaaS_Project_Management_System.Mappers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    TenantDto toDto(Tenant tenant);

    Tenant toEntity(TenantDto tenantDto);

    Tenant toEntity(TenantCreateDto tenantCreateDto);

    void updateEntityFromDto(TenantUpdateDto tenantUpdateDto, @MappingTarget Tenant tenant);
}