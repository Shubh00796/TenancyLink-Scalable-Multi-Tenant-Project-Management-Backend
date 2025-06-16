package com.Multi.tenant_SaaS_Project_Management_System.Mappers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserCreateDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserProfileDto;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserUpdateDto;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toDto(User user);

    List<UserDTO> toDtoList(List<User> users);

    User toEntity(UserCreateDto createDto);

    UserCreateDto toCreateDto(User user);


    void updateEntity(@MappingTarget User user, UserUpdateDto updateDto);

    void updateProfile(@MappingTarget User user, UserProfileDto profileDto);
}