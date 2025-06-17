package com.Multi.tenant_SaaS_Project_Management_System.Services;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskRequestDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskResponseDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskSummaryDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskUpdateDTO;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO requestDTO);

    Optional<TaskResponseDTO> getTaskById(Long id);

    Optional<TaskResponseDTO> getTaskByCode(String taskCode);

    TaskResponseDTO updateTask(Long id, TaskUpdateDTO updateDTO);

    void deleteTask(Long id);

    // Listing and pagination
    Page<TaskResponseDTO> getAllTasks(Pageable pageable);

    Page<TaskResponseDTO> getTasksByProject(Long projectId, Pageable pageable);

    Page<TaskResponseDTO> getTasksByAssignee(Long assigneeUserId, Pageable pageable);

    Page<TaskResponseDTO> getTasksByReporter(Long reporterUserId, Pageable pageable);

    Page<TaskResponseDTO> getTasksByStatus(TaskStatus status, Pageable pageable);

    Page<TaskResponseDTO> getTasksByPriority(TaskPriority priority, Pageable pageable);

    // Advanced filtering
    Page<TaskResponseDTO> getTasksWithFilters(Long projectId, Long assigneeId,
                                              TaskStatus status, TaskPriority priority,
                                              Pageable pageable);

    // Business logic methods
    TaskResponseDTO assignTask(Long taskId, Long assigneeUserId);

    TaskResponseDTO changeTaskStatus(Long taskId, TaskStatus newStatus);

    TaskResponseDTO updateTaskProgress(Long taskId, BigDecimal actualHours);

    // Query methods
    List<TaskSummaryDTO> getActiveTasksByAssignee(Long assigneeUserId);

    List<TaskSummaryDTO> getOverdueTasks();

    List<TaskSummaryDTO> getTasksDueSoon(int days);

    List<TaskSummaryDTO> getProjectTasksByStatus(Long projectId, TaskStatus status);

    // Validation methods
    boolean existsByTaskCode(String taskCode);

    long getTaskCountByProjectAndStatus(Long projectId, TaskStatus status);
}