package com.eventmanager.admin.controller;

import com.eventmanager.admin.dto.response.AdminStatisticsResponse;
import com.eventmanager.admin.dto.response.AdminUserResponse;
import com.eventmanager.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getUsers() {

        return ResponseEntity.ok(
                adminService.getUsers()
        );
    }

    @PostMapping("/users/{id}/block")
    public ResponseEntity<Void> blockUser(
            @PathVariable Long id
    ) {

        adminService.blockUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<Void> unblockUser(
            @PathVariable Long id
    ) {

        adminService.unblockUser(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<AdminStatisticsResponse> getStatistics() {

        return ResponseEntity.ok(
                adminService.getStatistics()
        );
    }
}