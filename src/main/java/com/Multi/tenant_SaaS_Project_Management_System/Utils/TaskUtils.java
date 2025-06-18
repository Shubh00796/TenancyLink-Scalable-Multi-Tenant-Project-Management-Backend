package com.Multi.tenant_SaaS_Project_Management_System.Utils;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.Task;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.TaskStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class TaskUtils {

    public static boolean canTransitionTo(Task task, TaskStatus newStatus) {
        return switch (task.getStatus()) {
            case TODO -> newStatus == TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> newStatus == TaskStatus.DONE || newStatus == TaskStatus.TODO;
            case DONE -> newStatus == TaskStatus.IN_PROGRESS;
        };
    }

    public static boolean isOverdue(Task task) {
        return task.getDueDate() != null &&
                task.getDueDate().isBefore(LocalDate.now()) &&
                task.getStatus() != TaskStatus.DONE;
    }

    public static boolean isCompleted(Task task) {
        return task.getStatus() == TaskStatus.DONE;
    }
}
