package com.transitops.controller;

import com.transitops.dto.VehicleCreateDTO;
import com.transitops.dto.VehicleResponseDTO;
import com.transitops.enums.VehicleType;
import com.transitops.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Vehicle management operations.
 *
 * <p>Implements the endpoints specified in Phase 4, Step 4.1 of the gap analysis.
 * RBAC enforcement follows the HLD RBAC matrix:
 * <ul>
 *   <li><strong>FLEET_MANAGER</strong>: Full CRUD (create, update, retire)</li>
 *   <li><strong>All authenticated roles</strong>: Read-only access (list, get by ID)</li>
 * </ul>
 * </p>
 *
 * <p>All write operations produce friendly error responses:
 * <ul>
 *   <li>Duplicate registration number → 409 Conflict</li>
 *   <li>Referenced region not found → 404 Not Found</li>
 *   <li>Retiring an ON_TRIP vehicle → 409 Conflict</li>
 *   <li>Validation failures → 400 Bad Request with field-level errors</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Constructs a VehicleController with the required service dependency.
     *
     * @param vehicleService the vehicle business logic service
     */
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }


    // =========================================================================
    //  CREATE — POST /api/vehicles
    // =========================================================================

    /**
     * Creates a new vehicle in the fleet.
     *
     * <p>Only Fleet Managers can add vehicles. The vehicle starts with status
     * AVAILABLE and odometer at zero.</p>
     *
     * @param dto validated creation payload
     * @return 201 Created with the new vehicle's details
     */
    @PostMapping
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<VehicleResponseDTO> create(@Valid @RequestBody VehicleCreateDTO dto) {
        VehicleResponseDTO created = vehicleService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // =========================================================================
    //  LIST — GET /api/vehicles
    // =========================================================================

    /**
     * Returns a paginated, optionally filtered list of vehicles.
     *
     * <p>All authenticated users can view the fleet for their own workflows
     * (drivers selecting a vehicle for a trip, analysts reviewing utilization, etc.).
     * Supports optional query parameters for filtering:</p>
     * <ul>
     *   <li>{@code vehicleType} — filter by vehicle type enum (TRUCK, VAN, etc.)</li>
     *   <li>{@code status} — filter by operational status (AVAILABLE, ON_TRIP, etc.)</li>
     *   <li>{@code regionId} — filter by assigned region</li>
     *   <li>{@code maintenanceDueWithinDays} — filter for upcoming maintenance (gap #13)</li>
     * </ul>
     *
     * @param vehicleType              optional vehicle type filter
     * @param status                   optional status filter
     * @param regionId                 optional region ID filter
     * @param maintenanceDueWithinDays optional upcoming maintenance filter
     * @param pageable                 pagination/sorting (page, size, sort query params)
     * @return page of vehicle response DTOs
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<VehicleResponseDTO> list(
            @RequestParam(required = false) VehicleType vehicleType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Integer maintenanceDueWithinDays,
            Pageable pageable) {
        // Convert enum to its String name for the Specification-based query
        String vehicleTypeStr = (vehicleType != null) ? vehicleType.name() : null;
        return vehicleService.list(vehicleTypeStr, status, regionId, maintenanceDueWithinDays, pageable);
    }


    // =========================================================================
    //  GET BY ID — GET /api/vehicles/{id}
    // =========================================================================

    /**
     * Retrieves a single vehicle by its ID.
     *
     * @param id the vehicle ID
     * @return 200 OK with the vehicle's details, or 404 if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VehicleResponseDTO> getById(@PathVariable Long id) {
        VehicleResponseDTO vehicle = vehicleService.getById(id);
        return ResponseEntity.ok(vehicle);
    }


    // =========================================================================
    //  UPDATE — PUT /api/vehicles/{id}
    // =========================================================================

    /**
     * Updates an existing vehicle's details.
     *
     * <p>Only Fleet Managers can modify vehicle records. Updates are blocked
     * if the vehicle is currently ON_TRIP.</p>
     *
     * @param id  the vehicle ID
     * @param dto validated update payload
     * @return 200 OK with the updated vehicle's details
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<VehicleResponseDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody VehicleCreateDTO dto) {
        VehicleResponseDTO updated = vehicleService.update(id, dto);
        return ResponseEntity.ok(updated);
    }


    // =========================================================================
    //  RETIRE — PATCH /api/vehicles/{id}/retire
    // =========================================================================

    /**
     * Retires a vehicle from the active fleet.
     *
     * <p>Retired vehicles are excluded from fleet utilization calculations and
     * cannot be assigned to new trips. This action is blocked if the vehicle
     * is currently ON_TRIP.</p>
     *
     * @param id the vehicle ID
     * @return 204 No Content on success
     */
    @PatchMapping("/{id}/retire")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<Void> retire(@PathVariable Long id) {
        vehicleService.retire(id);
        return ResponseEntity.noContent().build();
    }
}
