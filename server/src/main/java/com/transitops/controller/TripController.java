package com.transitops.controller;

import com.transitops.dto.*;
import com.transitops.security.AuthUser;
import com.transitops.service.TripStateMachineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Trip lifecycle management.
 *
 * <p>Implements all trip endpoints specified in Phase 5 of the gap analysis,
 * including the full FSM (create draft, dispatch, complete, cancel, abandon)
 * and checkpoint management.</p>
 *
 * <p>RBAC enforcement per the gap #4 resolution:
 * <ul>
 *   <li><strong>DRIVER only</strong>: Create draft trips (per PDF §2)</li>
 *   <li><strong>DRIVER + FLEET_MANAGER</strong>: Dispatch, complete, cancel, abandon,
 *       manage checkpoints, refuel. FLEET_MANAGER has oversight privilege (can act on
 *       any trip), while DRIVER can only act on their own trips (gap #3 fix).</li>
 *   <li><strong>All authenticated</strong>: Read-only access (list, get by ID)</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripStateMachineService tripService;

    public TripController(TripStateMachineService tripService) {
        this.tripService = tripService;
    }


    // =========================================================================
    //  CREATE DRAFT — POST /api/trips
    // =========================================================================

    /**
     * Creates a new trip in DRAFT status.
     *
     * <p>Restricted to DRIVER role only (gap #4 resolution: PDF §2 assigns trip
     * creation exclusively to drivers).</p>
     *
     * @param dto         validated trip creation payload
     * @param currentUser the authenticated driver creating the trip
     * @return 201 Created with the new trip details
     */
    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<TripResponseDTO> createDraftTrip(
            @Valid @RequestBody TripCreateDTO dto,
            @AuthenticationPrincipal AuthUser currentUser) {
        TripResponseDTO created = tripService.createDraft(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // =========================================================================
    //  DISPATCH — POST /api/trips/{id}/dispatch
    // =========================================================================

    /**
     * Dispatches a DRAFT trip, locking vehicle and driver assets.
     *
     * <p>Re-validates driver compliance at dispatch time (gap #2 fix).
     * Both DRIVER (own trip) and FLEET_MANAGER (any trip) can dispatch.</p>
     *
     * @param id          the trip ID to dispatch
     * @param currentUser the authenticated user performing the dispatch
     * @return 200 OK with dispatch confirmation details
     */
    @PostMapping("/{id}/dispatch")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<DispatchedTripResponseDTO> dispatchTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser currentUser) {
        DispatchedTripResponseDTO response = tripService.dispatch(id, currentUser);
        return ResponseEntity.ok(response);
    }


    // =========================================================================
    //  COMPLETE — POST /api/trips/{id}/complete
    // =========================================================================

    /**
     * Completes a DISPATCHED trip, releasing vehicle and driver assets.
     *
     * <p>Requires a final odometer reading that must be ≥ the start odometer.</p>
     *
     * @param id          the trip ID to complete
     * @param dto         contains the end odometer reading
     * @param currentUser the authenticated user completing the trip
     * @return 200 OK with the completed trip details
     */
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<TripResponseDTO> completeTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripCompleteDTO dto,
            @AuthenticationPrincipal AuthUser currentUser) {
        TripResponseDTO response = tripService.complete(id, dto, currentUser);
        return ResponseEntity.ok(response);
    }


    // =========================================================================
    //  CANCEL — POST /api/trips/{id}/cancel (gap #1 fix)
    // =========================================================================

    /**
     * Cancels a DISPATCHED trip, releasing vehicle and driver assets.
     *
     * <p><strong>Gap #1 fix:</strong> This endpoint was entirely missing from
     * the original LLD. Both DRIVER (own trip) and FLEET_MANAGER (override)
     * can cancel.</p>
     *
     * @param id          the trip ID to cancel
     * @param dto         contains the mandatory cancellation reason
     * @param currentUser the authenticated user cancelling the trip
     * @return 204 No Content on success
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<Void> cancelTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripCancelDTO dto,
            @AuthenticationPrincipal AuthUser currentUser) {
        tripService.cancel(id, dto, currentUser);
        return ResponseEntity.noContent().build();
    }


    // =========================================================================
    //  ABANDON DRAFT — DELETE /api/trips/{id}
    // =========================================================================

    /**
     * Deletes/abandons a DRAFT trip.
     *
     * <p>Distinct from cancel — this endpoint is for DRAFT trips only (no assets
     * are locked). Attempting to delete a non-DRAFT trip returns an error directing
     * the caller to use the cancel endpoint instead.</p>
     *
     * @param id          the trip ID to abandon
     * @param currentUser the authenticated user abandoning the draft
     * @return 204 No Content on success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<Void> abandonDraftTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser currentUser) {
        tripService.abandonDraft(id, currentUser);
        return ResponseEntity.noContent().build();
    }


    // =========================================================================
    //  ADD CHECKPOINTS — POST /api/trips/{id}/checkpoints (gap #11 fix)
    // =========================================================================

    /**
     * Adds ordered waypoints (checkpoints) to a trip.
     *
     * <p><strong>Gap #11 fix:</strong> The refuel-at-checkpoint flow requires
     * checkpoints to exist, but nothing in the original design created them.</p>
     *
     * @param id          the trip ID
     * @param dtos        list of checkpoint creation payloads
     * @param currentUser the authenticated user adding checkpoints
     * @return 201 Created with the list of created checkpoints
     */
    @PostMapping("/{id}/checkpoints")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<List<CheckpointResponseDTO>> addCheckpoints(
            @PathVariable Long id,
            @Valid @RequestBody List<CheckpointCreateDTO> dtos,
            @AuthenticationPrincipal AuthUser currentUser) {
        List<CheckpointResponseDTO> created = tripService.addCheckpoints(id, dtos, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // =========================================================================
    //  GET CHECKPOINTS — GET /api/trips/{id}/checkpoints
    // =========================================================================

    /**
     * Retrieves all checkpoints for a trip, ordered by their sequence number.
     *
     * @param id the trip ID
     * @return 200 OK with the ordered list of checkpoints
     */
    @GetMapping("/{id}/checkpoints")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CheckpointResponseDTO>> getCheckpoints(@PathVariable Long id) {
        List<CheckpointResponseDTO> checkpoints = tripService.getCheckpoints(id);
        return ResponseEntity.ok(checkpoints);
    }


    // =========================================================================
    //  REFUEL AT CHECKPOINT — POST /api/trips/{id}/refuel
    // =========================================================================

    /**
     * Logs a refuel event at a specific checkpoint of an active trip.
     *
     * <p>Creates a FuelLog record and marks the checkpoint as ARRIVED with
     * the odometer reading and timestamp.</p>
     *
     * @param id          the trip ID
     * @param dto         refuel details (checkpoint, fuel quantity, cost, odometer)
     * @param currentUser the authenticated user logging the refuel
     * @return 201 Created on success
     */
    @PostMapping("/{id}/refuel")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<Void> refuelAtCheckpoint(
            @PathVariable Long id,
            @Valid @RequestBody RefuelAtCheckpointDTO dto,
            @AuthenticationPrincipal AuthUser currentUser) {
        tripService.logCheckpointRefuel(id, dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // =========================================================================
    //  LIST TRIPS — GET /api/trips
    // =========================================================================

    /**
     * Returns a paginated list of trips with optional filtering.
     *
     * @param driverId optional filter by driver ID
     * @param status   optional filter by trip status
     * @param pageable pagination and sorting parameters
     * @return page of trip response DTOs
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<TripResponseDTO> listTrips(
            @RequestParam(required = false) Long driverId,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return tripService.listTrips(driverId, status, pageable);
    }


    // =========================================================================
    //  GET TRIP — GET /api/trips/{id}
    // =========================================================================

    /**
     * Retrieves a single trip by its ID.
     *
     * @param id the trip ID
     * @return 200 OK with the trip details, or 404 if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TripResponseDTO> getById(@PathVariable Long id) {
        TripResponseDTO trip = tripService.getById(id);
        return ResponseEntity.ok(trip);
    }
}
