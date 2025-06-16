package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.*;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.User;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Mappers.UserMapper;
import com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices.UserRepositoryService;
import com.Multi.tenant_SaaS_Project_Management_System.Services.UserService;
import com.Multi.tenant_SaaS_Project_Management_System.Utils.UserPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepositoryService repositoryService;
    private final UserMapper mapper;
    private final UserPermissionService permissionService;

    @Override
    @Transactional
    public UserDTO createUser(UserCreateDto createDto) {
        validateUser(createDto);

        if (repositoryService.existsByEmail(createDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + createDto.getEmail());
        }

        final User entity = mapper.toEntity(createDto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setStatus(UserStatus.ACTIVE);
        entity.setPasswordHash(createDto.getPasswordHash());


        User savedUser = repositoryService.save(entity);


        log.info("User created with id: {}", savedUser.getId());
        return mapper.toDto(savedUser);
    }

    private static void validateUser(final UserCreateDto createDto) {
        Objects.requireNonNull(createDto.getEmail(), "Email cannot be null");
        Objects.requireNonNull(createDto.getFirstName(), "First name cannot be null");
        Objects.requireNonNull(createDto.getLastName(), "Last name cannot be null");
    }

    @Override
    public UserDTO getUserById(final Long id) {
        return mapper.toDto(repositoryService.findById(id));
    }

    @Override
    public UserDTO getUserByEmail(final String email) {
        User user = repositoryService.findByEmail(email);
        log.info("rmial" + email);
        return mapper.toDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return repositoryService.findAllUsers()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAllUsers(final Pageable pageable) {
        return repositoryService.findAllPagabaleUsers(pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public UserDTO updateUser(final Long id, final UserUpdateDto updateDto) {
        final User user = getUser(id);

        // Defensive: Validate updateDto fields as needed (optional)

        user.setUpdatedAt(LocalDateTime.now());
        mapper.updateEntity(user, updateDto);

        User updatedUser = repositoryService.update(user);

        log.info("User updated with id: {}", id);
        return mapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(final Long id) {
        // Ensure user exists before deleting for clarity
        repositoryService.getByIdOrThrow(id);
        repositoryService.deleteById(id);
        log.info("User deleted with id: {}", id);
    }

    @Override
    @Transactional
    public void activateUser(final Long id) {
        User user = getUser(id);
        if (user.getStatus() != UserStatus.ACTIVE) {
            user.setStatus(UserStatus.ACTIVE);
            repositoryService.save(user);
            log.info("User activated with id: {}", id);
        }
    }

    @Override
    @Transactional
    public void deactivateUser(final Long id) {
        User user = getUser(id);
        if (user.getStatus() != UserStatus.INACTIVE) {
            user.setStatus(UserStatus.INACTIVE);
            repositoryService.save(user);
            log.info("User deactivated with id: {}", id);
        }
    }

    @Override
    @Transactional
    public UserDTO changeUserStatus(final Long id, final UserStatus status) {
        User user = getUser(id);
        if (!Objects.equals(user.getStatus(), status)) {
            user.setStatus(status);
            User updatedUser = repositoryService.save(user);
            log.info("User status changed to {} for id: {}", status, id);
            return mapper.toDto(updatedUser);
        }
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDTO changeUserRole(final Long id, final UserRole role) {
        User user = getUser(id);
        if (!Objects.equals(user.getRole(), role)) {
            user.setRole(role);
            User updatedUser = repositoryService.save(user);
            log.info("User role changed to {} for id: {}", role, id);
            return mapper.toDto(updatedUser);
        }
        return mapper.toDto(user);
    }

    @Override
    public List<UserDTO> getUsersByRole(final UserRole role) {
        return repositoryService.findByRoleIn(List.of(role))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByStatus(final UserStatus status) {
        return repositoryService.findByStatus(status)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByRoleAndStatus(final UserRole role, final UserStatus status) {
        return repositoryService.findByRoleAndStatus(role, status)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getActiveUsersByRoles(final List<UserRole> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return repositoryService.findByRoleInAndStatus(roles, UserStatus.ACTIVE)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void changePassword(final Long id, final UserPasswordUpdateDto passwordUpdateDto) {
        User user = getUser(id);

        repositoryService.save(user);
        log.info("Password changed for user id: {}", id);
    }

    @Override
    @Transactional
    public void resetPassword(final Long id, final String newPassword) {
        User user = getUser(id);
        // Hash the new password before setting
        // user.setPassword(passwordEncoder.encode(newPassword));
        repositoryService.save(user);
        log.info("Password reset for user id: {}", id);
    }

    @Override
    @Transactional
    public UserDTO updateProfile(final Long id, final UserProfileDto profileDto) {
        User user = getUser(id);
        mapper.updateProfile(user, profileDto);
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = repositoryService.save(user);
        log.info("Profile updated for user id: {}", id);
        return mapper.toDto(updatedUser);
    }

    @Override
    public List<UserDTO> searchUsersByName(final String name) {

        return repositoryService.findByFullNameContaining(name)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> searchUsersByFirstOrLastName(String firstName, String lastName) {
        if (isEmptyInput(firstName, lastName)) {
            return List.of();
        }
        return findUsers(firstName, lastName);
    }


    @Override
    public long countUsersByStatus(final UserStatus status) {
        return repositoryService.countByStatus(status);
    }

    @Override
    public long countUsersByRole(final UserRole role) {
        return repositoryService.countByRole(role);
    }

    @Override
    public long countUsersByRoleAndStatus(final UserRole role, final UserStatus status) {
        return repositoryService.countByRoleAndStatus(role, status);
    }

    @Override
    public long getTotalUsersCount() {
        return repositoryService.findAllUsers().size();
    }

    @Override
    public long getActiveUsersCount() {
        return countUsersByStatus(UserStatus.ACTIVE);
    }

    @Override
    public long getInactiveUsersCount() {
        return countUsersByStatus(UserStatus.INACTIVE);
    }

    @Override
    public boolean canUserManageUsers(final Long userId) {
        User user = getUser(userId);
        UserDTO userDto = mapper.toDto(user);
        return permissionService.canManageUsers(userDto);
    }

    @Override
    public boolean canUserManageProjects(final Long userId) {
        User user = getUser(userId);
        UserDTO userDto = mapper.toDto(user);
        return permissionService.canManageProjects(userDto);
    }

    @Override
    public boolean hasUserRole(final Long userId, final UserRole role) {
        User user = getUser(userId);
        return role.equals(user.getRole());
    }

    @Override
    public boolean hasUserAnyRole(final Long userId, final UserRole... roles) {
        User user = getUser(userId);
        UserRole userRole = getUserByRole(user);
        if (validateHasAnyRole(roles, userRole)) return true;
        return false;
    }


    /**
     * Utility method to get User or throw ResourceNotFoundException
     */
    private User getUser(final Long id) {
        return repositoryService.getByIdOrThrow(id);
    }

    private static boolean validateHasAnyRole(UserRole[] roles, UserRole userRole) {
        for (UserRole role : roles) {
            if (role.equals(userRole)) {
                return true;
            }
        }
        return false;
    }

    private static UserRole getUserByRole(User user) {
        UserRole userRole = user.getRole();
        return userRole;
    }

    private boolean isEmptyInput(String firstName, String lastName) {
        return isBlank(firstName) && isBlank(lastName);
    }

    private boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    private List<UserDTO> findUsers(String firstName, String lastName) {
        String fn = firstName == null ? "" : firstName;
        String ln = lastName == null ? "" : lastName;

        return repositoryService.findByFirstNameContainingOrLastNameContaining(fn, ln, null)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
