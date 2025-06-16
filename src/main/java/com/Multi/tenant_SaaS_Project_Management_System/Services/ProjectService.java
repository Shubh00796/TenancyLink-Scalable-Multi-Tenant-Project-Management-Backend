package com.Multi.tenant_SaaS_Project_Management_System.Services;


import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectSummaryDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectDto createProject(ProjectCreateDto createDto, Long createdByUserId);

    ProjectDto getProjectById(Long id);

     ProjectDto getProjectByCode(String projectCode);

    Page<ProjectDto> getAllProjects(Pageable pageable);

    ProjectDto updateProject(Long id, ProjectUpdateDto updateDto);

    void deleteProject(Long id);

    // Status changes (Use ProjectBusinessLogic in implementation)
    ProjectDto changeProjectStatus(Long id, ProjectStatus status);

    // Queries by key attributes
    List<ProjectDto> getProjectsByStatus(ProjectStatus status);

    List<ProjectDto> getProjectsByPriority(ProjectPriority priority);

    List<ProjectDto> getProjectsByOwner(Long ownerUserId);

    List<ProjectDto> getProjectsByCreator(Long createdByUserId);

    // Business state queries
    List<ProjectSummaryDto> getActiveProjects();

    List<ProjectSummaryDto> getHighPriorityActiveProjects();

    List<ProjectSummaryDto> getOverdueProjects();

    // Search
    List<ProjectDto> searchProjects(String searchTerm);

    // Date-based queries
    List<ProjectDto> getProjectsEndingBetween(LocalDate startDate, LocalDate endDate);

    List<ProjectDto> getActiveProjectsOnDate(LocalDate date);

    // Validation
    boolean existsByProjectCode(String projectCode);

    boolean isProjectOwner(Long projectId, Long userId);

    // Summary statistics (add if really needed in future)
    // long getProjectCountByStatus(ProjectStatus status);
    // long getProjectCountByPriority(ProjectPriority priority);
    // long getProjectCountByOwner(Long ownerUserId);
}
