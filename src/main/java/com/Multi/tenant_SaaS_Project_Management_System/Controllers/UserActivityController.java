package com.Multi.tenant_SaaS_Project_Management_System.Controllers;



import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserActivityRequest;
import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserActivityResponse;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ApiResponse;
import com.Multi.tenant_SaaS_Project_Management_System.Services.UserActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
@Validated
public class UserActivityController {

    private final UserActivityService userActivityService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserActivityResponse>> logActivity(
            @Valid @RequestBody UserActivityRequest request) {

        UserActivityResponse response = userActivityService.logActivity(request);
        return ResponseEntity.ok(ApiResponse.ok("Activity logged successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<UserActivityResponse>>> getUserActivities(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<UserActivityResponse> responses = userActivityService.getUserActivities(userId, start, end);
        return ResponseEntity.ok(ApiResponse.ok("User activities fetched", responses));
    }

    @GetMapping("/type/{activityType}")
    public ResponseEntity<ApiResponse<List<UserActivityResponse>>> getActivitiesByType(
            @PathVariable String activityType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<UserActivityResponse> responses = userActivityService.getActivitiesByType(activityType, start, end);
        return ResponseEntity.ok(ApiResponse.ok("Activities by type fetched", responses));
    }
}
