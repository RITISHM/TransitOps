package com.transitops.controller;

import com.transitops.dto.DriverCreateDTO;
import com.transitops.dto.DriverResponseDTO;
import com.transitops.dto.DriverUpdateDTO;
import com.transitops.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for Driver management operations.
 *
 * <p>Implements the endpoints specified in Phase 4, Step 4.2 of the gap analysis.
 * RBAC enforcement follows the HLD RBAC matrix:
 * <ul>
 *   <li><strong>FLEET_MANAGER</strong>: Create, update drivers</li>
 *   <li><strong>SAFETY_OFFICER</strong>: Update safety scores (stub for Phase 9)</li>
 *   <li><strong>All authenticated roles</strong>: Read-only access (list, get by ID)</li>
 * </ul>
 * </p>
 *
 * <p>Driver creation is a compound operation that atomically creates both a
 * {@code User} (login credentials) and a {@code Driver} (operational profile)
 * in a single transaction.</p>
 */
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    /**
     * Constructs a DriverController with the required service dependency.
     *
     * @param driverService the driver business logic service
     */
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }


    // =========================================================================
    //  CREATE — POST /api/drivers
    // =========================================================================

    /**
     * Creates a new driver along with their linked user account.
     *
     * <p>Only Fleet Managers can onboard new drivers. The operation creates a
     * User (role=DRIVER, BCrypt-hashed password) and a Driver entity atomically.
     * If any step fails, the entire transaction rolls back.</p>
     *
     * @param dto validated creation payload containing both user and driver fields
     * @return 201 Created with the new driver's details
     */
    @PostMapping
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<DriverResponseDTO> create(@Valid @RequestBody DriverCreateDTO dto) {
        DriverResponseDTO created = driverService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // =========================================================================
    //  LIST — GET /api/drivers
    // =========================================================================

    /**
     * Returns a paginated, optionally filtered list of drivers.
     *
     * <p>All authenticated users can view drivers for their own workflows
     * (Fleet Managers reviewing driver availability, Safety Officers auditing
     * compliance, etc.).</p>
     *
     * @param status   optional filter — matches drivers.status exactly
     * @param pageable pagination/sorting (page, size, sort query params)
     * @return page of driver response DTOs
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<DriverResponseDTO> list(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return driverService.list(status, pageable);
    }


    // =========================================================================
    //  GET BY ID — GET /api/drivers/{id}
    // =========================================================================

    /**
     * Retrieves a single driver by their ID.
     *
     * @param id the driver ID
     * @return 200 OK with the driver's details, or 404 if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DriverResponseDTO> getById(@PathVariable Long id) {
        DriverResponseDTO driver = driverService.getById(id);
        return ResponseEntity.ok(driver);
    }


    // =========================================================================
    //  UPDATE — PUT /api/drivers/{id}
    // =========================================================================

    /**
     * Updates an existing driver's operational fields.
     *
     * <p>Only Fleet Managers can modify driver records. This endpoint updates
     * driver-specific fields (license, contact info, status) but not user-level
     * identity fields (name, email, password).</p>
     *
     * @param id  the driver ID
     * @param dto validated update payload
     * @return 200 OK with the updated driver's details
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<DriverResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody DriverUpdateDTO dto) {
        DriverResponseDTO updated = driverService.update(id, dto);
        return ResponseEntity.ok(updated);
    }


    // =========================================================================
    //  SAFETY SCORE — PATCH /api/drivers/{id}/safety-score
    // =========================================================================

    /**
     * Updates a driver's safety score.
     *
     * <p>This endpoint is reserved exclusively for Safety Officers. It is
     * currently a stub created in Phase 4 as a routing dependency for Phase 9,
     * which will implement full scoring logic (incident-based calculations,
     * threshold alerts, compliance notifications).</p>
     *
     * @param id          the driver ID
     * @param safetyScore the new safety score (0.00 – 100.00), passed as a
     *                    request parameter
     * @return 200 OK with the updated driver's details
     */
    @PatchMapping("/{id}/safety-score")
    @PreAuthorize("hasRole('SAFETY_OFFICER')")
    public ResponseEntity<DriverResponseDTO> updateSafetyScore(
            @PathVariable Long id,
            @RequestParam BigDecimal safetyScore) {
        DriverResponseDTO updated = driverService.updateSafetyScore(id, safetyScore);
        return ResponseEntity.ok(updated);
    }
}
