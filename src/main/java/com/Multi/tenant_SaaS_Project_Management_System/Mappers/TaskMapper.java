package com.Multi.tenant_SaaS_Project_Management_System.Mappers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskRequestDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskResponseDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskSummaryDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.TaskUpdateDTO;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.Task;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // Request DTO -> Entity
    Task toEntity(TaskRequestDTO dto);

    // Update DTO -> Update existing Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskUpdateDTO dto, @MappingTarget Task task);

    // Entity -> Response DTO
    TaskResponseDTO toResponseDto(Task task);

    // Entity -> Summary DTO
    TaskSummaryDTO toSummaryDto(Task task);


}