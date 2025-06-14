package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.TenantConfiguration;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import com.Multi.tenant_SaaS_Project_Management_System.Mappers.TenantConfigurationMapper;
import com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices.TenantConfigurationRepositoryService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.TenantConfigurationService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TenantConfigurationServiceImpl implements TenantConfigurationService {

    private final TenantConfigurationRepositoryService repositoryService;
    private final TenantConfigurationMapper mapper;
    private final TenantService tenantService;


    @Override
    @Transactional
    public TenantConfigurationDto createConfiguration(TenantConfigurationCreateDto createDto) {
        TenantDto tenant = tenantService.getTenantById(createDto.getTenantId());
        Optional.ofNullable(tenant).orElseThrow(() -> new ResourceNotFoundException("You cant create the configuration if tenant is not present" + tenant));
        validateCreateDto(createDto);
        TenantConfiguration entity = mapper.toEntity(createDto);
        TenantConfiguration saved = repositoryService.create(entity);
        return mapper.toDto(saved);
    }

    private void validateCreateDto(TenantConfigurationCreateDto createDto) {
        Objects.requireNonNull(createDto, "Create DTO cannot be null");
        Objects.requireNonNull(createDto.getTenantId(), "TenantId cannot be null");
        Objects.requireNonNull(createDto.getConfigKey(), "ConfigKey cannot be null");
        Objects.requireNonNull(createDto.getConfigValue(), "ConfigValue cannot be null");
        Objects.requireNonNull(createDto.getConfigType(), "ConfigType cannot be null");
    }

    @Override
    public Optional<TenantConfigurationDto> getConfigurationById(Long id) {
        if (id == null) return Optional.empty();
        return repositoryService.getAllByTenantId(id).stream()
                .filter(config -> config.getId().equals(id))
                .findFirst()
                .map(mapper::toDto);
    }

    @Override
    public Optional<TenantConfigurationDto> getConfigurationByTenantAndKey(Long tenantId, String configKey) {
        if (isBoolean1(tenantId, configKey)) return Optional.empty();
        try {
            TenantConfiguration config = repositoryService.getByTenantIdAndConfigKey(tenantId, configKey);
            return Optional.of(mapper.toDto(config));
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }

    private static boolean isABoolean(Long tenantId, String configKey) {
        return tenantId == null || configKey == null;
    }

    @Override
    public Optional<TenantConfigurationDto> getConfigurationByTenantCodeAndKey(String tenantCode, String configKey) {
        if (isABoolean(tenantCode, configKey)) return Optional.empty();
        try {
            TenantConfiguration config = repositoryService.getByTenantCodeAndConfigKey(tenantCode, configKey);
            return Optional.of(mapper.toDto(config));
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }

    private static boolean isABoolean(String tenantCode, String configKey) {
        return tenantCode == null || configKey == null;
    }

    @Override
    public List<TenantConfigurationDto> getConfigurationsByTenantId(Long tenantId) {
        if (tenantId == null) return List.of();
        List<TenantConfiguration> configs = repositoryService.getAllByTenantId(tenantId);
        return configs.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TenantConfigurationDto> getConfigurationsByTenantCode(String tenantCode) {
        if (tenantCode == null) return List.of();
        List<TenantConfiguration> configs = repositoryService.getByTenantCode(tenantCode);
        return configs.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TenantConfigurationDto> getConfigurationsByTenantIdAndType(Long tenantId, ConfigType configType) {
        if (isABoolean(tenantId, configType)) return List.of();
        List<TenantConfiguration> configs = repositoryService.getByTenantIdAndConfigType(tenantId, configType);
        return configs.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    private static boolean isABoolean(Long tenantId, ConfigType configType) {
        return tenantId == null || configType == null;
    }


    @Override
    @Transactional
    public void deleteConfiguration(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        // Assuming repositoryService can delete by ID, if not, add method
        repositoryService.getAllByTenantId(id).stream()
                .filter(config -> config.getId().equals(id))
                .findFirst()
                .ifPresent(config -> repositoryService.deleteByTenantIdAndConfigKey(config.getTenantId(), config.getConfigKey()));
    }

    @Override
    @Transactional
    public void deleteConfigurationByTenantAndKey(Long tenantId, String configKey) {
        Objects.requireNonNull(tenantId, "tenantId cannot be null");
        Objects.requireNonNull(configKey, "configKey cannot be null");
        repositoryService.deleteByTenantIdAndConfigKey(tenantId, configKey);
    }

    @Override
    public boolean existsByTenantIdAndConfigKey(Long tenantId, String configKey) {
        if (isBoolean1(tenantId, configKey)) return false;
        return repositoryService.existsByTenantIdAndConfigKey(tenantId, configKey);
    }

    private static boolean isBoolean1(Long tenantId, String configKey) {
        return isABoolean1(tenantId, configKey);
    }

    private static boolean isABoolean1(Long tenantId, String configKey) {
        return isABoolean(tenantId, configKey);
    }

    @Override
    public Object getConfigurationValue(Long tenantId, String configKey) {
        Optional<TenantConfigurationDto> dtoOpt = getConfigurationByTenantAndKey(tenantId, configKey);
        return dtoOpt.map(TenantConfigurationDto::getConfigValue).orElse(null);
    }

    @Override
    public String getConfigurationValueAsString(Long tenantId, String configKey, String defaultValue) {
        Object value = getObject(tenantId, configKey);
        return value != null ? value.toString() : defaultValue;
    }

    @Override
    public Boolean getConfigurationValueAsBoolean(Long tenantId, String configKey, Boolean defaultValue) {
        Object value = getObject(tenantId, configKey);
        Boolean value1 = getABoolean(value);
        if (value1 != null) return value1;
        return defaultValue;
    }

    private static Boolean getABoolean(Object value) {
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof String) return Boolean.parseBoolean((String) value);
        return null;
    }

    @Override
    public Integer getConfigurationValueAsInteger(Long tenantId, String configKey, Integer defaultValue) {
        Integer value = getInteger(tenantId, configKey);
        if (value != null) return value;
        return defaultValue;
    }

    private Integer getInteger(Long tenantId, String configKey) {
        Object value = getObject(tenantId, configKey);
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private Object getObject(Long tenantId, String configKey) {
        Object value = getConfigurationValue(tenantId, configKey);
        return value;
    }

    @Override
    public Double getConfigurationValueAsDouble(Long tenantId, String configKey, Double defaultValue) {
        Object value = getObject(tenantId, configKey);
        if (value instanceof Double) return (Double) value;
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }
}