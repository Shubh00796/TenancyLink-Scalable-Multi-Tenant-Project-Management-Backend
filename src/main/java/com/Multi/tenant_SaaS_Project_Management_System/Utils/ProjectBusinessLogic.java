package com.Multi.tenant_SaaS_Project_Management_System.Utils;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.Project;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectPriority;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.ProjectStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProjectBusinessLogic {

    public boolean isActive(Project project) {
        return ProjectStatus.IN_PROGRESS.equals(project.getStatus()) ||
                ProjectStatus.PLANNING.equals(project.getStatus());
    }

    public boolean isCompleted(Project project) {
        return ProjectStatus.COMPLETED.equals(project.getStatus());
    }

    public boolean isCancelled(Project project) {
        return ProjectStatus.CANCELLED.equals(project.getStatus());
    }

    public boolean isOverdue(Project project) {
        return project.getEndDate() != null &&
                project.getEndDate().isBefore(LocalDate.now()) &&
                !isCompleted(project);
    }

    public boolean isHighPriority(Project project) {
        return ProjectPriority.HIGH.equals(project.getPriority());
    }

    public boolean hasStarted(Project project) {
        return project.getStartDate() != null &&
                !project.getStartDate().isAfter(LocalDate.now());
    }

    public long getDurationInDays(Project project) {
        if (project.getStartDate() == null || project.getEndDate() == null) return 0;
        return project.getStartDate().until(project.getEndDate()).getDays();
    }

    public boolean isOwnedBy(Project project, Long userId) {
        return project.getOwnerUserId().equals(userId);
    }

    public boolean isCreatedBy(Project project, Long userId) {
        return project.getCreatedByUserId().equals(userId);
    }
}
