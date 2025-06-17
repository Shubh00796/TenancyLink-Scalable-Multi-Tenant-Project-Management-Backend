package com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.Task;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Repositories.TaskRepository;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskRepoService {

    private final TaskRepository taskRepository;

    // Find task by its ID
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    // Find task by its taskCode
    public Task findByTaskCode(String taskCode) {
        return taskRepository.findByTaskCode(taskCode)
                .orElseThrow(() -> new ResourceNotFoundException("Task with code " + taskCode + " not found"));
    }

    // Save a task (create or update)
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    // Delete task by its ID
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    // List tasks by project ID
    public Page<Task> findByProjectId(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable);
    }

    // List tasks by assignee user ID
    public Page<Task> findByAssigneeUserId(Long assigneeUserId, Pageable pageable) {
        return taskRepository.findByAssigneeUserId(assigneeUserId, pageable);
    }

    // List tasks by reporter user ID
    public Page<Task> findByReporterUserId(Long reporterUserId, Pageable pageable) {
        return taskRepository.findByReporterUserId(reporterUserId, pageable);
    }

    // List tasks by status
    public Page<Task> findByStatus(TaskStatus status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    // List tasks by priority
    public Page<Task> findByPriority(TaskPriority priority, Pageable pageable) {
        return taskRepository.findByPriority(priority, pageable);
    }

    // Find tasks with advanced filters (projectId, assigneeId, status, priority)
    public Page<Task> findTasksWithFilters(Long projectId, Long assigneeId, TaskStatus status, TaskPriority priority, Pageable pageable) {
        return taskRepository.findTasksWithFilters(projectId, assigneeId, status, priority, pageable);
    }

    // Find overdue tasks
    public List<Task> findOverdueTasks(LocalDate date) {
        return taskRepository.findOverdueTasks(date);
    }

    // Find tasks due between a date range
    public List<Task> findTasksDueBetween(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findTasksDueBetween(startDate, endDate);
    }

    // Count tasks by project and status
    public long countByProjectIdAndStatus(Long projectId, TaskStatus status) {
        return taskRepository.countByProjectIdAndStatus(projectId, status);
    }
}
