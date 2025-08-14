package com.Multi.tenant_SaaS_Project_Management_System.Repositories;


import com.Multi.tenant_SaaS_Project_Management_System.Entities.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    List<UserActivity> findByUserIdAndActivityTimeBetween(
            Long userId, LocalDateTime start, LocalDateTime end
    );

    List<UserActivity> findByActivityTypeAndActivityTimeBetween(
            String activityType, LocalDateTime start, LocalDateTime end
    );
}
