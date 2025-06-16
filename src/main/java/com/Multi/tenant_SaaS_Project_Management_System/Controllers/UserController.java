package com.Multi.tenant_SaaS_Project_Management_System.Controllers;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.*;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    // Create a new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Validated UserCreateDto createDto) {
        UserDTO createdUser = userService.createUser(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Get user by email
    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // Get all users (with pagination)
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserDTO> usersPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }

    // Update user by id
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody @Validated UserUpdateDto updateDto) {
        UserDTO updatedUser = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Activate user
    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    // Deactivate user
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    // Change user status
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDTO> changeUserStatus(@PathVariable Long id,
                                                    @RequestBody UserStatus status) {
        UserDTO user = userService.changeUserStatus(id, status);
        return ResponseEntity.ok(user);
    }

    // Change user role
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserDTO> changeUserRole(@PathVariable Long id,
                                                  @RequestParam UserRole role) {
        UserDTO user = userService.changeUserRole(id, role);
        return ResponseEntity.ok(user);
    }

    // Change password
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @RequestBody @Validated UserPasswordUpdateDto passwordUpdateDto) {
        userService.changePassword(id, passwordUpdateDto);
        return ResponseEntity.noContent().build();
    }

    // Reset password (admin action, likely)
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id,
                                              @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }

    // Update profile
    @PutMapping("/{id}/profile")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Long id,
                                                 @RequestBody @Validated UserProfileDto profileDto) {
        UserDTO updatedUser = userService.updateProfile(id, profileDto);
        return ResponseEntity.ok(updatedUser);
    }

    // Search users by name (full name)
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsersByName(@RequestParam String name) {
        List<UserDTO> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    // Search users by first or last name
    @GetMapping("/search-by-name")
    public ResponseEntity<List<UserDTO>> searchUsersByFirstOrLastName(@RequestParam(required = false) String firstName,
                                                                      @RequestParam(required = false) String lastName) {
        List<UserDTO> users = userService.searchUsersByFirstOrLastName(firstName, lastName);
        return ResponseEntity.ok(users);
    }

    // Get users by role
    @GetMapping("/by-role")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@RequestParam UserRole role) {
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    // Get users by status
    @GetMapping("/by-status")
    public ResponseEntity<List<UserDTO>> getUsersByStatus(@RequestParam UserStatus status) {
        List<UserDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    // Get users by role and status
    @GetMapping("/by-role-status")
    public ResponseEntity<List<UserDTO>> getUsersByRoleAndStatus(@RequestParam UserRole role,
                                                                 @RequestParam UserStatus status) {
        List<UserDTO> users = userService.getUsersByRoleAndStatus(role, status);
        return ResponseEntity.ok(users);
    }

    // Counts (total, active, inactive)
    @GetMapping("/count/total")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(userService.getTotalUsersCount());
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveUsersCount() {
        return ResponseEntity.ok(userService.getActiveUsersCount());
    }

    @GetMapping("/count/inactive")
    public ResponseEntity<Long> getInactiveUsersCount() {
        return ResponseEntity.ok(userService.getInactiveUsersCount());
    }

    // Permission checks
    @GetMapping("/{id}/can-manage-users")
    public ResponseEntity<Boolean> canUserManageUsers(@PathVariable Long id) {
        boolean canManage = userService.canUserManageUsers(id);
        return ResponseEntity.ok(canManage);
    }

    @GetMapping("/{id}/can-manage-projects")
    public ResponseEntity<Boolean> canUserManageProjects(@PathVariable Long id) {
        boolean canManage = userService.canUserManageProjects(id);
        return ResponseEntity.ok(canManage);
    }

    // Check if user has specific role
    @GetMapping("/{id}/has-role")
    public ResponseEntity<Boolean> hasUserRole(@PathVariable Long id,
                                               @RequestParam UserRole role) {
        boolean hasRole = userService.hasUserRole(id, role);
        return ResponseEntity.ok(hasRole);
    }

    // Check if user has any role (multiple roles)
    @GetMapping("/{id}/has-any-role")
    public ResponseEntity<Boolean> hasUserAnyRole(@PathVariable Long id,
                                                  @RequestParam List<UserRole> roles) {
        boolean hasAnyRole = userService.hasUserAnyRole(id, roles.toArray(new UserRole[0]));
        return ResponseEntity.ok(hasAnyRole);
    }
}
