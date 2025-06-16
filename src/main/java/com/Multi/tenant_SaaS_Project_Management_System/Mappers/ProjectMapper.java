package com.Multi.tenant_SaaS_Project_Management_System.Mappers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectSummaryDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.ProjectUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDto toDto(Project project);

    ProjectSummaryDto toSummaryDto(Project project);


    Project toEntity(ProjectCreateDto createDto);

    void updateEntity(@MappingTarget Project project, ProjectUpdateDto updateDto);
}