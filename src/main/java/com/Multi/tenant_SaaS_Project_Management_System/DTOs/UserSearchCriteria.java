package com.Multi.tenant_SaaS_Project_Management_System.DTOs;


import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSearchCriteria {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
}

