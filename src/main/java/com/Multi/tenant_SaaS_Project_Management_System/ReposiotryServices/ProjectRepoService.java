package com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.Project;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import com.Multi.tenant_SaaS_Project_Management_System.Repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectRepoService {

    private final ProjectRepository projectRepository;

    public Project findByProjectCode(String projectCode) {
        return projectRepository.findByProjectCode(projectCode).orElseThrow(()-> new ResourceNotFoundException("CODE NOT FOUND" + projectCode));
    }

    public List<Project> findByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }

    public List<Project> findByPriority(ProjectPriority priority) {
        return projectRepository.findByPriority(priority);
    }

    public List<Project> findByOwnerUserId(Long ownerUserId) {
        return projectRepository.findByOwnerUserId(ownerUserId);
    }

    public List<Project> findByCreatedByUserId(Long createdByUserId) {
        return projectRepository.findByCreatedByUserId(createdByUserId);
    }

    public boolean existsByProjectCode(String projectCode) {
        return projectRepository.existsByProjectCode(projectCode);
    }

    public List<Project> findByStatusAndPriority(ProjectStatus status, ProjectPriority priority) {
        return projectRepository.findByStatusAndPriority(status, priority);
    }

    public List<Project> findByOwnerUserIdAndStatus(Long ownerUserId, ProjectStatus status) {
        return projectRepository.findByOwnerUserIdAndStatus(ownerUserId, status);
    }

    public List<Project> findOverdueProjects(LocalDate date) {
        return projectRepository.findOverdueProjects(date);
    }

    public List<Project> findActiveProjectsOnDate(LocalDate date) {
        return projectRepository.findActiveProjectsOnDate(date);
    }

    public List<Project> findProjectsEndingBetween(LocalDate startDate, LocalDate endDate) {
        return projectRepository.findProjectsEndingBetween(startDate, endDate);
    }

    public List<Project> searchProjects(String search) {
        return projectRepository.searchProjects(search);
    }

    public List<Project> findActiveProjects() {
        return projectRepository.findActiveProjects();
    }

    public List<Project> findHighPriorityActiveProjects() {
        return projectRepository.findHighPriorityActiveProjects();
    }

    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project update(Project project) {
        return projectRepository.save(project);
    }

    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }
}
