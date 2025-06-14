package com.Multi.tenant_SaaS_Project_Management_System.Controllers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantConfigurationUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import com.Multi.tenant_SaaS_Project_Management_System.Services.TenantConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenant-configurations")
@RequiredArgsConstructor
@Validated
public class TenantConfigurationController {

    private final TenantConfigurationService configurationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TenantConfigurationDto createConfiguration(
            @Valid @RequestBody TenantConfigurationCreateDto createDto) {
        return configurationService.createConfiguration(createDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantConfigurationDto> getConfigurationById(@PathVariable("id") Long id) {
        Optional<TenantConfigurationDto> dto = configurationService.getConfigurationById(id);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tenant/{tenantId}/key/{configKey}")
    public ResponseEntity<TenantConfigurationDto> getByTenantAndKey(
            @PathVariable  Long tenantId,
            @PathVariable  String configKey) {

        Optional<TenantConfigurationDto> dto = configurationService.getConfigurationByTenantAndKey(tenantId, configKey);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tenant-code/{tenantCode}/key/{configKey}")
    public ResponseEntity<TenantConfigurationDto> getByTenantCodeAndKey(
            @PathVariable  String tenantCode,
            @PathVariable  String configKey) {

        Optional<TenantConfigurationDto> dto = configurationService.getConfigurationByTenantCodeAndKey(tenantCode, configKey);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tenant/{tenantId}")
    public List<TenantConfigurationDto> getByTenantId(@PathVariable  Long tenantId) {
        return configurationService.getConfigurationsByTenantId(tenantId);
    }

    @GetMapping("/tenant-code/{tenantCode}")
    public List<TenantConfigurationDto> getByTenantCode(@PathVariable String tenantCode) {
        return configurationService.getConfigurationsByTenantCode(tenantCode);
    }

    @GetMapping("/tenant/{tenantId}/type/{configType}")
    public List<TenantConfigurationDto> getByTenantIdAndConfigType(
            @PathVariable Long tenantId,
            @PathVariable ConfigType configType) {
        return configurationService.getConfigurationsByTenantIdAndType(tenantId, configType);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConfiguration(@PathVariable Long id) {
        configurationService.deleteConfiguration(id);
    }

    @DeleteMapping("/tenant/{tenantId}/key/{configKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByTenantAndKey(
            @PathVariable  Long tenantId,
            @PathVariable  String configKey) {
        configurationService.deleteConfigurationByTenantAndKey(tenantId, configKey);
    }

    @GetMapping("/tenant/{tenantId}/key/{configKey}/exists")
    public ResponseEntity<Boolean> existsByTenantIdAndConfigKey(
            @PathVariable Long tenantId,
            @PathVariable  String configKey) {
        boolean exists = configurationService.existsByTenantIdAndConfigKey(tenantId, configKey);
        return ResponseEntity.ok(exists);
    }
}
