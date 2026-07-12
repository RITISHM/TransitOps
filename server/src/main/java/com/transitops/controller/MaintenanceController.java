package com.transitops.controller;

import com.transitops.domain.MaintenanceLog;
import com.transitops.dto.MaintenanceCloseDTO;
import com.transitops.dto.MaintenanceCreateDTO;
import com.transitops.security.AuthUser;
import com.transitops.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing vehicle maintenance (fixes gap #10).
 */
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    /**
     * Opens a new maintenance ticket and marks the vehicle as IN_SHOP.
     */
    @PostMapping
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<MaintenanceLog> open(
            @Valid @RequestBody MaintenanceCreateDTO dto,
            @AuthenticationPrincipal AuthUser currentUser) {
        MaintenanceLog log = maintenanceService.openMaintenanceTicket(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(log);
    }

    /**
     * Closes an active maintenance ticket and releases the vehicle.
     */
    @PostMapping("/{id}/close")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<Void> close(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceCloseDTO dto) {
        maintenanceService.closeMaintenanceTicket(id, dto);
        return ResponseEntity.noContent().build();
    }
}
