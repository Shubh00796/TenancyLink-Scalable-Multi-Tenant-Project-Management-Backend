package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.*;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.Task;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import com.Multi.tenant_SaaS_Project_Management_System.Mappers.TaskMapper;
import com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices.TaskRepoService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.ProjectService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.TaskService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.UserService;
import com.Multi.tenant_SaaS_Project_Management_System.Utils.TaskUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepoService repoService;
    private final TaskMapper mapper;
    private final TaskUtils taskUtils;
    private final UserService userService;
    private final ProjectService projectService;

    /**
     * @param requestDTO
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        validateRequestDTOBeforeCreatingTheTask(requestDTO);
        Task entity = mapper.toEntity(requestDTO);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setStatus(TaskStatus.IN_PROGRESS);
      log.info("Actual hours: " + entity.getActualHours());

        return mapper.toResponseDto(repoService.save(entity));
    }


    /**
     * @param id
     * @return
     */
    @Override
    public Optional<TaskResponseDTO> getTaskById(Long id) {
        Task task = repoService.findById(id);
        return Optional.ofNullable(mapper.toResponseDto(task));
    }

    /**
     * @param taskCode
     * @return
     */
    @Override
    public Optional<TaskResponseDTO> getTaskByCode(String taskCode) {
        Task byTaskCode = repoService.findByTaskCode(taskCode);
        return Optional.ofNullable(mapper.toResponseDto(byTaskCode));
    }

    /**
     * @param id
     * @param updateDTO
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TaskResponseDTO updateTask(Long id, TaskUpdateDTO updateDTO) {
        Task task = repoService.findById(id);
        Optional.ofNullable(task).orElseThrow(() -> new ResourceNotFoundException("id can not be null" + id));
        validateRequestDTOBeforeUpdatingTheTask(updateDTO);
        task.setUpdatedAt(LocalDateTime.now());
        mapper.updateTaskFromDto(updateDTO, task);

        return mapper.toResponseDto(repoService.save(task));
    }

    /**
     * @param id
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteTask(Long id) {
        repoService.deleteById(id);

    }


    private Page<TaskResponseDTO> getTasks(Pageable pageable, Function<Pageable, Page<Task>> queryFunction) {
        return queryFunction.apply(pageable)
                .map(mapper::toResponseDto);
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return getTasks(pageable, repoService::findAll);
    }

    /**
     * @param projectId
     * @param pageable
     * @return
     */
    @Override
    public Page<TaskResponseDTO> getTasksByProject(Long projectId, Pageable pageable) {
        return getTasks(pageable, p -> repoService.findByProjectId(projectId, p));
    }

    /**
     * @param assigneeUserId
     * @param pageable
     * @return
     */
    @Override
    public Page<TaskResponseDTO> getTasksByAssignee(Long assigneeUserId, Pageable pageable) {
        return getTasks(pageable, p -> repoService.findByAssigneeUserId(assigneeUserId, p));
    }

    /**
     * @param reporterUserId
     * @param pageable
     * @return
     */
    @Override
    public Page<TaskResponseDTO> getTasksByReporter(Long reporterUserId, Pageable pageable) {
        return getTasks(pageable, pageable1 -> repoService.findByReporterUserId(reporterUserId, pageable));
    }

    /**
     * @param status
     * @param pageable
     * @return
     */
    @Override
    public Page<TaskResponseDTO> getTasksByStatus(TaskStatus status, Pageable pageable) {
        return getTasks(pageable, pageable1 -> repoService.findByStatus(status, pageable));
    }

    /**
     * @param priority
     * @param pageable
     * @return
     */
    @Override
    public Page<TaskResponseDTO> getTasksByPriority(TaskPriority priority, Pageable pageable) {
        return getTasks(pageable, p -> repoService.findByPriority(priority, pageable));
    }

    /**
     * @param projectId
     * @param assigneeId
     * @param status
     * @param priority
     * @param pageable
     * @return
     */
    @Override
    public Page<TaskResponseDTO> getTasksWithFilters(Long projectId, Long assigneeId, TaskStatus status, TaskPriority priority, Pageable pageable) {
        return getTasks(pageable, pageable1 -> repoService.findTasksWithFilters(projectId, assigneeId, status, priority, pageable));
    }

    /**
     * @param taskId
     * @param assigneeUserId
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TaskResponseDTO assignTask(Long taskId, Long assigneeUserId) {
        Task task = repoService.findById(taskId);
        Optional.ofNullable(task).orElseThrow(() -> new ResourceNotFoundException("id not found" + taskId));


        task.setAssigneeUserId(assigneeUserId);
        Task updatedTask = repoService.save(task);


        return mapper.toResponseDto(updatedTask);
    }

    /**
     * @param taskId
     * @param newStatus
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TaskResponseDTO changeTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = repoService.findById(taskId);
        Optional.ofNullable(task).orElseThrow(() ->
                new ResourceNotFoundException("Task not found with ID: " + taskId)
        );

        task.setStatus(newStatus);
        task.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponseDto(repoService.save(task));
    }

    /**
     * @param taskId
     * @param actualHours
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TaskResponseDTO updateTaskProgress(Long taskId, BigDecimal actualHours) {
        Task task = repoService.findById(taskId);
        Optional.ofNullable(task).orElseThrow(() ->
                new ResourceNotFoundException("Task not found with ID: " + taskId)
        );

        task.setActualHours(actualHours);
        task.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponseDto(repoService.save(task));
    }

    /**
     * @param assigneeUserId
     * @return
     */
    @Override
    public List<TaskSummaryDTO> getActiveTasksByAssignee(Long assigneeUserId, TaskStatus status) {
        List<Task> tasks = repoService.findActiveTasksByAssignee(assigneeUserId, status);
        return tasks.stream()
                .map(mapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    @Override
    public List<TaskSummaryDTO> getOverdueTasks() {
        List<Task> tasks = repoService.findOverdueTasks(LocalDate.from(LocalDateTime.now()));
        return tasks.stream()
                .map(mapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * @param days
     * @return
     */
    @Override
    public List<TaskSummaryDTO> getTasksDueSoon(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDateThreshold = now.plusDays(days);
        List<Task> tasks = repoService.findOverdueTasks(LocalDate.from(dueDateThreshold));
        return tasks.stream()
                .map(mapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * @param projectId
     * @param status
     * @return
     */
    @Override
    public List<TaskSummaryDTO> getProjectTasksByStatus(Long projectId, TaskStatus status) {
        List<Task> tasks = repoService.findByProjectIdAndStatus(projectId, status);
        return tasks.stream()
                .map(mapper::toSummaryDto)
                .collect(Collectors.toList());
    }


    /**
     * @param projectId
     * @param status
     * @return
     */
    @Override
    public long getTaskCountByProjectAndStatus(Long projectId, TaskStatus status) {
        return repoService.countByProjectIdAndStatus(projectId, status);
    }


    private void validateCommonFields(Object dto, Long assigneeUserId, Long reporterUserId, BigDecimal estimatedHours, String title) {
        Objects.requireNonNull(assigneeUserId, "ASSIGNEE_USER_ID CAN NOT BE NULL");
        Objects.requireNonNull(estimatedHours, "ESTIMATED_HOURS CAN NOT BE NULL");
        Objects.requireNonNull(title, "TITLE CAN NOT BE NULL");

        UserDTO assignerId = getUserByIdOrThrow(assigneeUserId, "Assigner_id not found");
        UserDTO reporterId = getUserByIdOrThrow(reporterUserId, "Reporter_id not found");
    }

    private void validateRequestDTOBeforeCreatingTheTask(TaskRequestDTO requestDTO) {
        Objects.requireNonNull(requestDTO.getTaskCode(), "TASK_CODE CAN NOT BE NULL");
        Objects.requireNonNull(requestDTO.getProjectId(), "PROJECT_ID CAN NOT BE NULL");
        validateCommonFields(requestDTO, requestDTO.getAssigneeUserId(), requestDTO.getReporterUserId(), requestDTO.getEstimatedHours(), requestDTO.getTitle());
    }

    private void validateRequestDTOBeforeUpdatingTheTask(TaskUpdateDTO updateDTO) {
        validateCommonFields(updateDTO, updateDTO.getAssigneeUserId(), updateDTO.getReporterUserId(), updateDTO.getEstimatedHours(), updateDTO.getTitle());
    }


    private UserDTO getUserByIdOrThrow(Long userId, String errorMessage) {
        UserDTO user = userService.getUserById(userId);
        return Optional.ofNullable(user)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage + " " + userId));
    }


}
