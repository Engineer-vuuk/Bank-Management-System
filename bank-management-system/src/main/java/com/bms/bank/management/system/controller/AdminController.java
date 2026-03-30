package com.bms.bank.management.system.controller;

import com.bms.bank.management.system.dto.AdminDashboardResponse;
import com.bms.bank.management.system.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // ✅ Restricts all routes to ADMIN role
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ✅ Get Admin Dashboard Data
    @GetMapping("/dashboard")
    public AdminDashboardResponse getAdminDashboard() {
        return adminService.getDashboardData();
    }
}
