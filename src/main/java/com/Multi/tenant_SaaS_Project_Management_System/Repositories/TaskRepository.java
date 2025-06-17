package com.Multi.tenant_SaaS_Project_Management_System.Repositories;


import com.Multi.tenant_SaaS_Project_Management_System.Entities.Task;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTaskCode(String taskCode);

    boolean existsByTaskCode(String taskCode);

    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Page<Task> findByAssigneeUserId(Long assigneeUserId, Pageable pageable);

    Page<Task> findByReporterUserId(Long reporterUserId, Pageable pageable);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByPriority(TaskPriority priority, Pageable pageable);

    @Query("""
        SELECT t FROM Task t 
        WHERE (:projectId IS NULL OR t.projectId = :projectId)
        AND (:assigneeId IS NULL OR t.assigneeUserId = :assigneeId)
        AND (:status IS NULL OR t.status = :status)
        AND (:priority IS NULL OR t.priority = :priority)
        """)
    Page<Task> findTasksWithFilters(@Param("projectId") Long projectId,
                                    @Param("assigneeId") Long assigneeId,
                                    @Param("status") TaskStatus status,
                                    @Param("priority") TaskPriority priority,
                                    Pageable pageable);

    // Overdue and due-range queries
    @Query("SELECT t FROM Task t WHERE t.dueDate <= :date AND t.status != 'DONE'")
    List<Task> findOverdueTasks(@Param("date") LocalDate date);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate AND t.status != 'DONE'")
    List<Task> findTasksDueBetween(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    // Count tasks for dashboard or reporting
    long countByProjectIdAndStatus(Long projectId, TaskStatus status);

    // Specific filter queries — useful for service-level logic
    @Query("SELECT t FROM Task t WHERE t.projectId = :projectId AND t.status = :status")
    List<Task> findByProjectIdAndStatus(@Param("projectId") Long projectId,
                                        @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.assigneeUserId = :userId AND t.status IN :statuses")
    List<Task> findActiveTasksByAssignee(@Param("userId") Long userId,
                                         @Param("statuses") List<TaskStatus> statuses);


}