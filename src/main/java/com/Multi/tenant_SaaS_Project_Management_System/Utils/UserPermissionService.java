package com.Multi.tenant_SaaS_Project_Management_System.Utils;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserDTO;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionService {

    public boolean isActive(UserDTO user) {
        return UserStatus.ACTIVE.equals(user.getStatus());
    }

    public boolean isAdmin(UserDTO user) {
        return UserRole.ADMIN.equals(user.getRole());
    }

    public boolean isManager(UserDTO user) {
        return UserRole.MANAGER.equals(user.getRole());
    }

    public boolean isMember(UserDTO user) {
        return UserRole.MEMBER.equals(user.getRole());
    }

    public boolean hasRole(UserDTO user, UserRole requiredRole) {
        return user.getRole().equals(requiredRole);
    }

    public boolean hasAnyRole(UserDTO user, UserRole... roles) {
        for (UserRole role : roles) {
            if (user.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean canManageUsers(UserDTO user) {
        return isAdmin(user) || isManager(user);
    }

    public boolean canManageProjects(UserDTO user) {
        return isAdmin(user) || isManager(user);
    }

    public String getFullName(UserDTO user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}