package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.*;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.Project;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import com.Multi.tenant_SaaS_Project_Management_System.Mappers.ProjectMapper;
import com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices.ProjectRepoService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.ProjectService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.UserService;
import com.Multi.tenant_SaaS_Project_Management_System.Utils.ProjectBusinessLogic;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepoService repoService;
    private final ProjectBusinessLogic businessLogic;
    private final ProjectMapper mapper;
    private final UserService userService;

    @Override
    @Transactional
    public ProjectDto createProject(ProjectCreateDto createDto, Long createdByUserId) {
        validateCreateProject(createDto);
        validateTheUserIds(createDto, createdByUserId);
        Project project = mapper.toEntity(createDto);
        project.setCreatedByUserId(createdByUserId);
        project.setCreatedAt(LocalDateTime.now());
        project.setStatus(ProjectStatus.IN_PROGRESS);
        Project savedProject = repoService.save(project);
        ProjectDto dto = mapper.toDto(savedProject);
        dto.setActive(businessLogic.isActive(savedProject));
        dto.setCompleted(businessLogic.isCompleted(savedProject));
        dto.setOverdue(businessLogic.isOverdue(savedProject));
        dto.setDurationInDays(businessLogic.getDurationInDays(savedProject));


        return dto;
    }


    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = repoService.findById(id);
        return mapper.toDto(project);
    }

    @Override
    public ProjectDto getProjectByCode(String projectCode) {
        return mapper.toDto(repoService.findByProjectCode(projectCode));
    }

    @Override
    public Page<ProjectDto> getAllProjects(Pageable pageable) {
        return repoService.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public ProjectDto updateProject(Long id, ProjectUpdateDto updateDto) {
//        ProjectDto project = getProjectById(id);
        Project project = repoService.findById(id);
        Optional.ofNullable(project).orElseThrow(() -> new ResourceNotFoundException("THE PROJECT ID CAN NOT BE NULL OR EMPTY" + id));
        project.setUpdatedAt(LocalDateTime.now());
        UserDTO ownerUser = userService.getUserById(updateDto.getOwnerUserId());
        Optional.ofNullable(ownerUser)
                .orElseThrow(() -> new ResourceNotFoundException("Owner user id not found: " + updateDto.getOwnerUserId()));
        mapper.updateEntity(project, updateDto);
        return mapper.toDto(repoService.update(project));
    }

    @Override

    public void deleteProject(Long id) {
        Project project = Optional.ofNullable(repoService.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        repoService.deleteById(id);
    }

    @Override
    @Transactional
    public ProjectDto changeProjectStatus(Long id, ProjectStatus status) {
        Project project = Optional.ofNullable(repoService.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        project.setStatus(status);
        project.setUpdatedAt(LocalDateTime.now());

        Project updatedProject = repoService.update(project);
        return mapper.toDto(updatedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByStatus(ProjectStatus status) {
        return mapList(repoService.findByStatus(status), mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByPriority(ProjectPriority priority) {
        return mapList(repoService.findByPriority(priority), mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByOwner(Long ownerUserId) {
        return mapList(repoService.findByOwnerUserId(ownerUserId), mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByCreator(Long createdByUserId) {
        return mapList(repoService.findByCreatedByUserId(createdByUserId), mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getActiveProjects() {
        return mapList(repoService.findActiveProjects(), mapper::toSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getHighPriorityActiveProjects() {
        return mapList(repoService.findHighPriorityActiveProjects(), mapper::toSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getOverdueProjects() {
        LocalDate today = LocalDate.now();

        List<Project> overdueProjects = repoService.findOverdueProjects(today);

        return overdueProjects.stream()
                .map(mapper::toSummaryDto)  // map Project → ProjectSummaryDto
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> searchProjects(String searchTerm) {

        return mapList(repoService.searchProjects(searchTerm), mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsEndingBetween(LocalDate startDate, LocalDate endDate) {
        return mapList(repoService.findProjectsEndingBetween(startDate, endDate), mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getActiveProjectsOnDate(LocalDate date) {
        return mapList(repoService.findActiveProjectsOnDate(date), mapper::toDto);
    }

    @Override
    public boolean existsByProjectCode(String projectCode) {
        return repoService.existsByProjectCode(projectCode);
    }

    @Override
    public boolean isProjectOwner(Long projectId, Long userId) {
        Project project = repoService.findById(projectId);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return businessLogic.isOwnedBy(project, userId);
    }

    /**
     * Utility method to get User or throw ResourceNotFoundException
     */


    private void validateTheUserIds(ProjectCreateDto createDto, Long createdByUserId) {
        UserDTO user = requireNonNullOrThrow(userService.getUserById(createdByUserId), "user id not found" + createdByUserId);
        UserDTO ownerUser = userService.getUserById(createDto.getOwnerUserId());
        Optional.ofNullable(ownerUser)
                .orElseThrow(() -> new ResourceNotFoundException("Owner user id not found: " + createDto.getOwnerUserId()));
    }

    private static void validateCreateProject(ProjectCreateDto createDto) {
        Objects.requireNonNull(createDto.getProjectCode(), "Project code can not be null");
        Objects.requireNonNull(createDto.getName(), "Name can not be null");
        Objects.requireNonNull(createDto.getOwnerUserId(), "OwnerID can not be null");
        Objects.requireNonNull(createDto.getEndDate(), "Enddate can not be null");
    }

    private <T, R> List<R> mapList(List<T> list, Function<T, R> mapperFunc) {
        return list.stream()
                .map(mapperFunc)
                .collect(Collectors.toList());
    }
    private <T> T requireNonNullOrThrow(T obj, String errorMessage) {
        return Optional.ofNullable(obj).orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }

}
