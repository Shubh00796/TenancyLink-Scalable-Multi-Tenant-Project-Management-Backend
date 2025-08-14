package com.Multi.tenant_SaaS_Project_Management_System.Entities;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String activityType; // e.g., LOGIN, LOGOUT, FILE_UPLOAD

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime activityTime;
}
