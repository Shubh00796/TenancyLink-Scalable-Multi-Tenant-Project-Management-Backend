package com.Multi.tenant_SaaS_Project_Management_System.Controllers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TenantUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TenantStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Services.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@Validated
public class TenantController {

    private final TenantService tenantService;

    // Create a new tenant
    @PostMapping
    public ResponseEntity<TenantDto> createTenant(@Valid @RequestBody TenantCreateDto createDto) {
        TenantDto tenantDto = tenantService.createTenant(createDto);
        return new ResponseEntity<>(tenantDto, HttpStatus.CREATED);
    }

    // Get tenant by ID
    @GetMapping("/{id}")
    public ResponseEntity<TenantDto> getTenantById(@PathVariable Long id) {
        TenantDto tenantDto = tenantService.getTenantById(id);
        return ResponseEntity.ok(tenantDto);
    }

    // Get tenant by Code
    @GetMapping("/code/{tenantCode}")
    public ResponseEntity<TenantDto> getTenantByCode(@PathVariable String tenantCode) {
        TenantDto tenantDto = tenantService.getTenantByCode(tenantCode);
        return ResponseEntity.ok(tenantDto);
    }



    // Get all tenants
    @GetMapping
    public ResponseEntity<List<TenantDto>> getAllTenants() {
        List<TenantDto> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    // Get tenants paginated
    @GetMapping("/page")
    public ResponseEntity<Page<TenantDto>> getAllTenantsByPage(Pageable pageable) {
        Page<TenantDto> tenantPage = tenantService.getAllTenantsByPage(pageable);
        return ResponseEntity.ok(tenantPage);
    }

    // Get tenants by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TenantDto>> getTenantsByStatus(@PathVariable TenantStatus status) {
        List<TenantDto> tenants = tenantService.getTenantsByStatus(status);
        return ResponseEntity.ok(tenants);
    }

    // Update tenant
    @PutMapping("/{id}")
    public ResponseEntity<TenantDto> updateTenant(@PathVariable Long id,
                                                  @Valid @RequestBody TenantUpdateDto updateDto) {
        TenantDto updatedTenant = tenantService.updateTenant(id, updateDto);
        return ResponseEntity.ok(updatedTenant);
    }

    // Delete tenant
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
    }

    // Deactivate tenant
    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateTenant(@PathVariable Long id) {
        tenantService.deactivateTenant(id);
    }

    // Activate tenant
    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateTenant(@PathVariable Long id) {
        tenantService.activateTenant(id);
    }

    // Suspend tenant
    @PostMapping("/{id}/suspend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspendTenant(@PathVariable Long id) {
        tenantService.suspendTenant(id);
    }

    // Check if tenant exists by code
    @GetMapping("/exists/code/{tenantCode}")
    public ResponseEntity<Boolean> existsByTenantCode(@PathVariable String tenantCode) {
        boolean exists = tenantService.existsByTenantCode(tenantCode);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/database/{databaseName}")
    public ResponseEntity<Boolean> existsByDatabaseName(@PathVariable String databaseName) {
        boolean exists = tenantService.existsByDatabaseName(databaseName);
        return ResponseEntity.ok(exists);
    }

    // Count tenants by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countTenantsByStatus(@PathVariable TenantStatus status) {
        long count = tenantService.countTenantsByStatus(status);
        return ResponseEntity.ok(count);
    }

    // Check if tenant is active by tenantCode
    @GetMapping("/active/{tenantCode}")
    public ResponseEntity<Boolean> isTenantActive(@PathVariable String tenantCode) {
        boolean active = tenantService.isTenantActive(tenantCode);
        return ResponseEntity.ok(active);
    }

    // Check if tenant can add users
    @GetMapping("/{tenantId}/can-add-users")
    public ResponseEntity<Boolean> canTenantAddUsers(@PathVariable Long tenantId,
                                                     @RequestParam int currentUserCount) {
        boolean canAdd = tenantService.canTenantAddUsers(tenantId, currentUserCount);
        return ResponseEntity.ok(canAdd);
    }

    // Check if tenant can add projects
    @GetMapping("/{tenantId}/can-add-projects")
    public ResponseEntity<Boolean> canTenantAddProjects(@PathVariable Long tenantId,
                                                        @RequestParam int currentProjectCount) {
        boolean canAdd = tenantService.canTenantAddProjects(tenantId, currentProjectCount);
        return ResponseEntity.ok(canAdd);
    }
}
