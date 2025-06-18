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

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    public Task findByTaskCode(String taskCode) {
        return taskRepository.findByTaskCode(taskCode)
                .orElseThrow(() -> new ResourceNotFoundException("Task with code " + taskCode + " not found"));
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Page<Task> findByProjectId(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable);
    }

    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Page<Task> findByAssigneeUserId(Long assigneeUserId, Pageable pageable) {
        return taskRepository.findByAssigneeUserId(assigneeUserId, pageable);
    }

    public List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status);
    }

    public List<Task> findActiveTasksByAssignee(Long assigneeUserId, TaskStatus status) {
        return taskRepository.findByProjectIdAndStatus(assigneeUserId, status);
    }



    // List tasks by reporter user ID
    public Page<Task> findByReporterUserId(Long reporterUserId, Pageable pageable) {
        return taskRepository.findByReporterUserId(reporterUserId, pageable);
    }

    public Page<Task> findByStatus(TaskStatus status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    public Page<Task> findByPriority(TaskPriority priority, Pageable pageable) {
        return taskRepository.findByPriority(priority, pageable);
    }

    public Page<Task> findTasksWithFilters(Long projectId, Long assigneeId, TaskStatus status, TaskPriority priority, Pageable pageable) {
        return taskRepository.findTasksWithFilters(projectId, assigneeId, status, priority, pageable);
    }

    public List<Task> findOverdueTasks(LocalDate date) {
        return taskRepository.findOverdueTasks(date);
    }

    public List<Task> findTasksDueBetween(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findTasksDueBetween(startDate, endDate);
    }

    public long countByProjectIdAndStatus(Long projectId, TaskStatus status) {
        return taskRepository.countByProjectIdAndStatus(projectId, status);
    }


}
