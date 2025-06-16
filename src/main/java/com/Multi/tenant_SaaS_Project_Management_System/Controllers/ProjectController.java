package com.Multi.tenant_SaaS_Project_Management_System.Controllers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.*;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectCreateDto createDto,
            @RequestParam Long createdByUserId) {

        ProjectDto projectDto = projectService.createProject(createDto, createdByUserId);
        return new ResponseEntity<>(projectDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto dto = projectService.getProjectById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/code/{projectCode}")
    public ResponseEntity<ProjectDto> getProjectByCode(@PathVariable String projectCode) {
        ProjectDto dto = projectService.getProjectByCode(projectCode);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProjectDto>> getAllProjects(Pageable pageable) {
        Page<ProjectDto> projects = projectService.getAllProjects(pageable);
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateDto updateDto) {

        ProjectDto updatedProject = projectService.updateProject(id, updateDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectDto> changeProjectStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {

        ProjectDto dto = projectService.changeProjectStatus(id, status);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectDto>> getProjectsByStatus(@PathVariable ProjectStatus status) {
        List<ProjectDto> projects = projectService.getProjectsByStatus(status);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<ProjectDto>> getProjectsByPriority(@PathVariable ProjectPriority priority) {
        List<ProjectDto> projects = projectService.getProjectsByPriority(priority);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/owner/{ownerUserId}")
    public ResponseEntity<List<ProjectDto>> getProjectsByOwner(@PathVariable Long ownerUserId) {
        List<ProjectDto> projects = projectService.getProjectsByOwner(ownerUserId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/creator/{createdByUserId}")
    public ResponseEntity<List<ProjectDto>> getProjectsByCreator(@PathVariable Long createdByUserId) {
        List<ProjectDto> projects = projectService.getProjectsByCreator(createdByUserId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProjectSummaryDto>> getActiveProjects() {
        List<ProjectSummaryDto> projects = projectService.getActiveProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/active/high-priority")
    public ResponseEntity<List<ProjectSummaryDto>> getHighPriorityActiveProjects() {
        List<ProjectSummaryDto> projects = projectService.getHighPriorityActiveProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<ProjectSummaryDto>> getOverdueProjects() {
        List<ProjectSummaryDto> projects = projectService.getOverdueProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectDto>> searchProjects(@RequestParam String searchTerm) {
        List<ProjectDto> projects = projectService.searchProjects(searchTerm);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/ending-between")
    public ResponseEntity<List<ProjectDto>> getProjectsEndingBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<ProjectDto> projects = projectService.getProjectsEndingBetween(startDate, endDate);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/active-on")
    public ResponseEntity<List<ProjectDto>> getActiveProjectsOnDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<ProjectDto> projects = projectService.getActiveProjectsOnDate(date);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByProjectCode(@RequestParam String projectCode) {
        boolean exists = projectService.existsByProjectCode(projectCode);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{projectId}/is-owner/{userId}")
    public ResponseEntity<Boolean> isProjectOwner(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        boolean isOwner = projectService.isProjectOwner(projectId, userId);
        return ResponseEntity.ok(isOwner);
    }
}
