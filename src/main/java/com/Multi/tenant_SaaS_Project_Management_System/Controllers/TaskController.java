package com.Multi.tenant_SaaS_Project_Management_System.Controllers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskRequestDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskResponseDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskSummaryDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskUpdateDTO;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // CREATE TASK
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO createdTask = taskService.createTask(requestDTO);
        return ResponseEntity.ok(createdTask);
    }

    // GET TASK BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET TASK BY CODE
    @GetMapping("/code/{taskCode}")
    public ResponseEntity<TaskResponseDTO> getTaskByCode(@PathVariable String taskCode) {
        return taskService.getTaskByCode(taskCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE TASK
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO updateDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, updateDTO));
    }

    // DELETE TASK
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // GET ALL TASKS
    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    // GET TASKS BY PROJECT
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByProject(@PathVariable Long projectId, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, pageable));
    }

    // GET TASKS BY ASSIGNEE
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByAssignee(@PathVariable Long assigneeId, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByAssignee(assigneeId, pageable));
    }

    // GET TASKS BY REPORTER
    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByReporter(@PathVariable Long reporterId, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByReporter(reporterId, pageable));
    }

    // GET TASKS BY STATUS
    @GetMapping("/status")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByStatus(@RequestParam TaskStatus status, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status, pageable));
    }

    // GET TASKS BY PRIORITY
    @GetMapping("/priority")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByPriority(@RequestParam TaskPriority priority, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority, pageable));
    }

    // FILTERED SEARCH
    @GetMapping("/filter")
    public ResponseEntity<Page<TaskResponseDTO>> getFilteredTasks(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            Pageable pageable) {

        return ResponseEntity.ok(taskService.getTasksWithFilters(projectId, assigneeId, status, priority, pageable));
    }

    // ASSIGN TASK
    @PostMapping("/{taskId}/assign/{assigneeId}")
    public ResponseEntity<TaskResponseDTO> assignTask(
            @PathVariable Long taskId,
            @PathVariable Long assigneeId) {
        return ResponseEntity.ok(taskService.assignTask(taskId, assigneeId));
    }

    // CHANGE TASK STATUS
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> changeTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.changeTaskStatus(taskId, status));
    }

    // UPDATE PROGRESS
    @PatchMapping("/{taskId}/progress")
    public ResponseEntity<TaskResponseDTO> updateTaskProgress(
            @PathVariable Long taskId,
            @RequestParam BigDecimal actualHours) {
        return ResponseEntity.ok(taskService.updateTaskProgress(taskId, actualHours));
    }

    // ACTIVE TASKS BY ASSIGNEE
    @GetMapping("/active")
    public ResponseEntity<List<TaskSummaryDTO>> getActiveTasksByAssignee(
            @RequestParam Long assigneeId,
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.getActiveTasksByAssignee(assigneeId, status));
    }

    // OVERDUE TASKS
    @GetMapping("/overdue")
    public ResponseEntity<List<TaskSummaryDTO>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }

    // TASKS DUE SOON
    @GetMapping("/due-soon")
    public ResponseEntity<List<TaskSummaryDTO>> getTasksDueSoon(@RequestParam int days) {
        return ResponseEntity.ok(taskService.getTasksDueSoon(days));
    }

    // PROJECT TASKS BY STATUS
    @GetMapping("/project/{projectId}/status")
    public ResponseEntity<List<TaskSummaryDTO>> getProjectTasksByStatus(
            @PathVariable Long projectId,
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.getProjectTasksByStatus(projectId, status));
    }

    // COUNT TASKS BY PROJECT AND STATUS
    @GetMapping("/project/{projectId}/status/count")
    public ResponseEntity<Long> getTaskCountByProjectAndStatus(
            @PathVariable Long projectId,
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.getTaskCountByProjectAndStatus(projectId, status));
    }
}
