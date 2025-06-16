package com.Multi.tenant_SaaS_Project_Management_System.Repositories;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.Project;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectCode(String projectCode);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByPriority(ProjectPriority priority);

    List<Project> findByOwnerUserId(Long ownerUserId);

    List<Project> findByCreatedByUserId(Long createdByUserId);

    boolean existsByProjectCode(String projectCode);

    // Combined filters for common use cases
    List<Project> findByStatusAndPriority(ProjectStatus status, ProjectPriority priority);

    List<Project> findByOwnerUserIdAndStatus(Long ownerUserId, ProjectStatus status);

    // Date-based queries
    @Query("SELECT p FROM Project p WHERE p.endDate < :date AND p.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Project> findOverdueProjects(@Param("date") LocalDate date);

    @Query("SELECT p FROM Project p WHERE p.startDate <= :date AND p.endDate >= :date")
    List<Project> findActiveProjectsOnDate(@Param("date") LocalDate date);

    @Query("SELECT p FROM Project p WHERE p.endDate BETWEEN :startDate AND :endDate")
    List<Project> findProjectsEndingBetween(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    // Search queries
    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.projectCode) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Project> searchProjects(@Param("search") String search);

    // Active projects (PLANNING, IN_PROGRESS) - commonly used business states
    @Query("SELECT p FROM Project p WHERE p.status IN ('PLANNING', 'IN_PROGRESS')")
    List<Project> findActiveProjects();

    @Query("SELECT p FROM Project p WHERE p.priority = 'HIGH' AND p.status IN ('PLANNING', 'IN_PROGRESS')")
    List<Project> findHighPriorityActiveProjects();
}