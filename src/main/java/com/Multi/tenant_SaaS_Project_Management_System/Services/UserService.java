package com.Multi.tenant_SaaS_Project_Management_System.Services;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.*;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // CRUD Operations
    UserDTO createUser(UserCreateDto createDto);
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    List<UserDTO> getAllUsers();
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO updateUser(Long id, UserUpdateDto updateDto);
    void deleteUser(Long id);

    // User Status Management
    void activateUser(Long id);
    void deactivateUser(Long id);
    UserDTO changeUserStatus(Long id, UserStatus status);

    // Role Management
    UserDTO changeUserRole(Long id, UserRole role);
    List<UserDTO> getUsersByRole(UserRole role);
    List<UserDTO> getUsersByStatus(UserStatus status);
    List<UserDTO> getUsersByRoleAndStatus(UserRole role, UserStatus status);
    List<UserDTO> getActiveUsersByRoles(List<UserRole> roles);

    // Authentication & Authorization

    void changePassword(Long id, UserPasswordUpdateDto passwordUpdateDto);
    void resetPassword(Long id, String newPassword);

    // Profile Management
    UserDTO updateProfile(Long id, UserProfileDto profileDto);

    // Search & Query Operations
    List<UserDTO> searchUsersByName(String name);
    List<UserDTO> searchUsersByFirstOrLastName(String firstName, String lastName);

    // Statistics & Counts
    long countUsersByStatus(UserStatus status);
    long countUsersByRole(UserRole role);
    long countUsersByRoleAndStatus(UserRole role, UserStatus status);
    long getTotalUsersCount();
    long getActiveUsersCount();
    long getInactiveUsersCount();

    // Business Logic
    boolean canUserManageUsers(Long userId);
    boolean canUserManageProjects(Long userId);
    boolean hasUserRole(Long userId, UserRole role);
    boolean hasUserAnyRole(Long userId, UserRole... roles);
}
