package com.transitops.controller;

import com.transitops.dto.DashboardKpiDTO;
import com.transitops.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for retrieving Dashboard KPIs.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Retrieves the high-level Key Performance Indicators for the fleet dashboard.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public DashboardKpiDTO getDashboard() {
        return dashboardService.getDashboardKpis();
    }
}
