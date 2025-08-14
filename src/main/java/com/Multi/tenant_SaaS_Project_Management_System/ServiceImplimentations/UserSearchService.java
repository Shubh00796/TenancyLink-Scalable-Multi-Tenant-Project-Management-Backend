package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserDTO;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserSearchCriteria;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.User;
import com.Multi.tenant_SaaS_Project_Management_System.Mappers.UserMapper;
import com.Multi.tenant_SaaS_Project_Management_System.Repositories.UserRepository;
import com.Multi.tenant_SaaS_Project_Management_System.Utils.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> searchUsers(UserSearchCriteria criteria, int page, int size) {
        Specification<User> spec = Specification.allOf(
                UserSpecifications.firstNameContains(criteria.getFirstName()),
                UserSpecifications.lastNameContains(criteria.getLastName()),
                UserSpecifications.fullNameContains(criteria.getFullName()),
                UserSpecifications.emailContains(criteria.getEmail()),
                UserSpecifications.roleEquals(criteria.getRole()),
                UserSpecifications.statusEquals(criteria.getStatus()),
                UserSpecifications.createdAfter(criteria.getCreatedAfter()),
                UserSpecifications.createdBefore(criteria.getCreatedBefore())
        );

        Page<User> userPage = userRepository.findAll(spec, PageRequest.of(page, size));
        return userPage.getContent().stream()
                .map(userMapper::toDto)
                .toList();
    }


}
