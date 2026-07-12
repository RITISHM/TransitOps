package com.transitops.service;

import com.transitops.domain.*;
import com.transitops.dto.*;
import com.transitops.enums.DriverStatus;
import com.transitops.enums.TripStatus;
import com.transitops.enums.VehicleStatus;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.*;
import com.transitops.security.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementing the Trip finite state machine (FSM).
 *
 * <p>Manages the complete lifecycle of a trip through its states:
 * <pre>
 *   DRAFT → DISPATCHED → COMPLETED
 *                │
 *                └→ CANCELLED
 *   DRAFT → [deleted/abandoned]
 * </pre>
 *
 * <p>This service addresses the following gaps from the gap analysis:
 * <ul>
 *   <li><strong>Gap #1:</strong> CANCELLED transition — {@link #cancel}</li>
 *   <li><strong>Gap #2:</strong> Dispatch-time compliance re-validation — {@link #dispatch}</li>
 *   <li><strong>Gap #3:</strong> Row-level authorization — {@link #assertCallerCanActOnTrip}</li>
 *   <li><strong>Gap #11:</strong> Checkpoint creation — {@link #addCheckpoints}</li>
 * </ul>
 * </p>
 */
@Service
public class TripStateMachineService {

    private static final Logger log = LoggerFactory.getLogger(TripStateMachineService.class);

    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripCheckpointRepository checkpointRepository;
    private final FuelLogRepository fuelLogRepository;

    public TripStateMachineService(TripRepository tripRepository,
                                   VehicleRepository vehicleRepository,
                                   DriverRepository driverRepository,
                                   TripCheckpointRepository checkpointRepository,
                                   FuelLogRepository fuelLogRepository) {
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.checkpointRepository = checkpointRepository;
        this.fuelLogRepository = fuelLogRepository;
    }


    // =========================================================================
    //  STEP 5.1 — Create Draft (DRIVER-only, gap #4 resolution)
    // =========================================================================

    /**
     * Creates a new trip in DRAFT status.
     *
     * <p>Performs initial validation at draft-creation time:
     * <ul>
     *   <li>Vehicle must exist and be AVAILABLE</li>
     *   <li>Driver must exist and be AVAILABLE</li>
     *   <li>Driver must not be suspended and license must not be expired</li>
     *   <li>Cargo weight must not exceed the vehicle's maximum load capacity</li>
     * </ul>
     *
     * <p>Note: These checks are repeated at dispatch time (Step 5.2) to close
     * the compliance loophole where conditions may change between draft and dispatch.</p>
     *
     * <p>If the authenticated driver is creating a draft for a different driver,
     * this is logged but not blocked (product decision: drivers may draft on behalf
     * of co-drivers).</p>
     *
     * @param dto         the validated trip creation payload
     * @param currentUser the authenticated user creating the draft
     * @return response DTO of the newly created trip
     */
    @Transactional
    public TripResponseDTO createDraft(TripCreateDTO dto, AuthUser currentUser) {
        // Resolve and validate Vehicle
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with ID: " + dto.getVehicleId()));

        if (!VehicleStatus.AVAILABLE.name().equals(vehicle.getStatus())) {
            throw new IllegalStateException(
                    "Vehicle '" + vehicle.getRegistrationNumber()
                            + "' is not available (current status: " + vehicle.getStatus() + ").");
        }

        // Resolve and validate Driver
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found with ID: " + dto.getDriverId()));

        if (!DriverStatus.AVAILABLE.name().equals(driver.getStatus())) {
            throw new IllegalStateException(
                    "Driver is not available (current status: " + driver.getStatus() + ").");
        }

        // Compliance check: suspension and license expiry
        if (DriverStatus.SUSPENDED.name().equals(driver.getStatus())) {
            throw new IllegalStateException("Driver is currently suspended and cannot be assigned to trips.");
        }
        if (driver.getLicenseExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException(
                    "Driver's license expired on " + driver.getLicenseExpiryDate()
                            + ". Cannot assign to a trip.");
        }

        // Cargo capacity check
        if (dto.getCargoWeight() != null
                && dto.getCargoWeight().compareTo(vehicle.getMaxLoadCapacity()) > 0) {
            throw new IllegalArgumentException(
                    "Cargo weight (" + dto.getCargoWeight() + " kg) exceeds vehicle's max load capacity ("
                            + vehicle.getMaxLoadCapacity() + " kg).");
        }

        // Log if the authenticated driver is drafting for a different driver
        Driver callerDriver = driverRepository.findByUserId(currentUser.getId()).orElse(null);
        if (callerDriver != null && !callerDriver.getId().equals(driver.getId())) {
            log.info("Driver {} (user {}) created a draft trip for a different driver {} (user {}). "
                            + "Flagged for product review — not blocked.",
                    callerDriver.getId(), currentUser.getId(),
                    driver.getId(), driver.getUser().getId());
        }

        // Build and persist the DRAFT trip
        Trip trip = new Trip();
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setSource(dto.getSource());
        trip.setDestination(dto.getDestination());
        trip.setExpectedDistance(dto.getExpectedDistance());
        trip.setCargoWeight(dto.getCargoWeight());
        trip.setRevenue(dto.getRevenue());
        trip.setStatus(TripStatus.DRAFT.name());

        Trip saved = tripRepository.save(trip);
        return TripResponseDTO.fromEntity(saved);
    }


    // =========================================================================
    //  STEP 5.2 — Dispatch (gap #2 fix: compliance re-validation)
    // =========================================================================

    /**
     * Dispatches a DRAFT trip, locking the vehicle and driver for active use.
     *
     * <p><strong>Gap #2 fix:</strong> Re-validates driver compliance (license expiry,
     * suspension status) and cargo capacity at dispatch time, not just at draft time.
     * This closes the loophole where a driver's license could expire or they could be
     * suspended between draft creation and dispatch.</p>
     *
     * <p>Uses pessimistic locking on both Vehicle and Driver to prevent concurrent
     * dispatch of the same assets.</p>
     *
     * @param tripId      the ID of the trip to dispatch
     * @param currentUser the authenticated user performing the dispatch
     * @return lightweight confirmation DTO
     */
    @Transactional
    public DispatchedTripResponseDTO dispatch(Long tripId, AuthUser currentUser) {
        Trip trip = tripRepository.findByIdForUpdate(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));

        // State guard: only DRAFT trips can be dispatched
        if (!TripStatus.DRAFT.name().equals(trip.getStatus())) {
            throw new IllegalStateException(
                    "Only DRAFT trips can be dispatched. Current status: " + trip.getStatus());
        }

        // Row-level authorization (gap #3 fix)
        assertCallerCanActOnTrip(trip, currentUser);

        // Acquire pessimistic locks on vehicle and driver to prevent concurrent dispatch
        Vehicle vehicle = vehicleRepository.findByIdForUpdate(trip.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
        Driver driver = driverRepository.findByIdForUpdate(trip.getDriver().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found."));

        // Asset availability check
        if (!VehicleStatus.AVAILABLE.name().equals(vehicle.getStatus())) {
            throw new IllegalStateException(
                    "Vehicle '" + vehicle.getRegistrationNumber()
                            + "' is no longer available (status: " + vehicle.getStatus() + ").");
        }
        if (!DriverStatus.AVAILABLE.name().equals(driver.getStatus())) {
            throw new IllegalStateException(
                    "Driver is no longer available (status: " + driver.getStatus() + ").");
        }

        // *** GAP #2 FIX — Re-validate compliance AT DISPATCH TIME ***
        if (DriverStatus.SUSPENDED.name().equals(driver.getStatus())) {
            throw new IllegalStateException("Driver failed compliance re-validation at dispatch time: SUSPENDED.");
        }
        if (driver.getLicenseExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException(
                    "Driver failed compliance re-validation at dispatch time: license expired on "
                            + driver.getLicenseExpiryDate() + ".");
        }

        // Re-validate cargo capacity (weight could have been adjusted on the vehicle record)
        if (trip.getCargoWeight() != null
                && trip.getCargoWeight().compareTo(vehicle.getMaxLoadCapacity()) > 0) {
            throw new IllegalArgumentException(
                    "Cargo weight exceeds vehicle capacity — re-validated at dispatch.");
        }

        // Transition: lock assets and activate the trip
        vehicle.setStatus(VehicleStatus.ON_TRIP.name());
        driver.setStatus(DriverStatus.ON_TRIP.name());
        trip.setStatus(TripStatus.DISPATCHED.name());
        trip.setDispatchedAt(LocalDateTime.now());
        trip.setStartOdometer(vehicle.getCurrentOdometer());

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);
        tripRepository.save(trip);

        return new DispatchedTripResponseDTO(
                trip.getId(),
                trip.getStatus(),
                vehicle.getRegistrationNumber(),
                driver.getUser().getName());
    }


    // =========================================================================
    //  STEP 5.3 — Complete
    // =========================================================================

    /**
     * Completes a DISPATCHED trip, releasing the vehicle and driver.
     *
     * <p>Validates that the end odometer reading is greater than or equal to the
     * start odometer (captured at dispatch). Updates the vehicle's current odometer
     * to the end reading for accurate tracking.</p>
     *
     * @param tripId      the ID of the trip to complete
     * @param dto         contains the final odometer reading
     * @param currentUser the authenticated user completing the trip
     * @return response DTO of the completed trip
     */
    @Transactional
    public TripResponseDTO complete(Long tripId, TripCompleteDTO dto, AuthUser currentUser) {
        Trip trip = tripRepository.findByIdForUpdate(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));

        // State guard: only DISPATCHED trips can be completed
        if (!TripStatus.DISPATCHED.name().equals(trip.getStatus())) {
            throw new IllegalStateException(
                    "Only DISPATCHED trips can be completed. Current status: " + trip.getStatus());
        }

        // Row-level authorization (gap #3 fix)
        assertCallerCanActOnTrip(trip, currentUser);

        // Odometer validation: end must be ≥ start
        if (trip.getStartOdometer() != null
                && dto.getEndOdometer().compareTo(trip.getStartOdometer()) < 0) {
            throw new IllegalArgumentException(
                    "End odometer (" + dto.getEndOdometer()
                            + ") cannot be less than start odometer (" + trip.getStartOdometer() + ").");
        }

        // Release vehicle and driver back to AVAILABLE
        Vehicle vehicle = vehicleRepository.findByIdForUpdate(trip.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
        Driver driver = driverRepository.findByIdForUpdate(trip.getDriver().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found."));

        vehicle.setStatus(VehicleStatus.AVAILABLE.name());
        vehicle.setCurrentOdometer(dto.getEndOdometer());  // Update vehicle's odometer
        driver.setStatus(DriverStatus.AVAILABLE.name());
        trip.setStatus(TripStatus.COMPLETED.name());
        trip.setEndOdometer(dto.getEndOdometer());
        trip.setCompletedAt(LocalDateTime.now());

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);
        tripRepository.save(trip);

        return TripResponseDTO.fromEntity(trip);
    }


    // =========================================================================
    //  STEP 5.4a — Cancel (gap #1 fix: DISPATCHED → CANCELLED)
    // =========================================================================

    /**
     * Cancels a DISPATCHED trip, releasing the vehicle and driver.
     *
     * <p><strong>Gap #1 fix:</strong> The FSM diagram, PDF §4, and ER diagram's
     * cancelled_at column all require a cancel transition, but the original LLD
     * never implemented it. This method closes that gap.</p>
     *
     * <p>This is distinct from {@link #abandonDraft} — cancellation only applies
     * to DISPATCHED trips where assets are locked. Attempting to cancel a DRAFT
     * trip returns an error directing the caller to use DELETE instead.</p>
     *
     * @param tripId      the ID of the trip to cancel
     * @param dto         contains the mandatory cancellation reason
     * @param currentUser the authenticated user cancelling the trip
     */
    @Transactional
    public void cancel(Long tripId, TripCancelDTO dto, AuthUser currentUser) {
        Trip trip = tripRepository.findByIdForUpdate(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));

        // State guard: only DISPATCHED trips can be cancelled
        if (!TripStatus.DISPATCHED.name().equals(trip.getStatus())) {
            throw new IllegalStateException(
                    "Only DISPATCHED trips can be cancelled. "
                            + "Use DELETE /api/trips/{id} for DRAFT trips. Current status: " + trip.getStatus());
        }

        // Row-level authorization: DRIVER (self) or FLEET_MANAGER (override)
        assertCallerCanActOnTrip(trip, currentUser);

        // Release vehicle and driver back to AVAILABLE
        Vehicle vehicle = vehicleRepository.findByIdForUpdate(trip.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
        Driver driver = driverRepository.findByIdForUpdate(trip.getDriver().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found."));

        vehicle.setStatus(VehicleStatus.AVAILABLE.name());
        driver.setStatus(DriverStatus.AVAILABLE.name());
        trip.setStatus(TripStatus.CANCELLED.name());
        trip.setCancelledAt(LocalDateTime.now());
        trip.setCancellationReason(dto.getReason());

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);
        tripRepository.save(trip);

        log.info("Trip {} cancelled by user {}. Reason: {}",
                tripId, currentUser.getId(), dto.getReason());
    }


    // =========================================================================
    //  STEP 5.4b — Abandon Draft (DRAFT → deleted)
    // =========================================================================

    /**
     * Deletes a DRAFT trip entirely.
     *
     * <p>Unlike cancellation, abandoning a draft has no asset side-effects because
     * vehicle and driver statuses are never locked for a DRAFT trip. The trip row
     * is simply removed from the database.</p>
     *
     * @param tripId      the ID of the draft trip to abandon
     * @param currentUser the authenticated user abandoning the draft
     */
    @Transactional
    public void abandonDraft(Long tripId, AuthUser currentUser) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));

        // State guard: only DRAFT trips can be abandoned
        if (!TripStatus.DRAFT.name().equals(trip.getStatus())) {
            throw new IllegalStateException(
                    "Only DRAFT trips can be deleted/abandoned. "
                            + "Use POST /api/trips/{id}/cancel for DISPATCHED trips. Current status: " + trip.getStatus());
        }

        // Row-level authorization
        assertCallerCanActOnTrip(trip, currentUser);

        // No asset side-effects — assets were never locked for a DRAFT trip
        tripRepository.delete(trip);

        log.info("Draft trip {} abandoned by user {}.", tripId, currentUser.getId());
    }


    // =========================================================================
    //  STEP 5.5 — Row-Level Authorization (gap #3 fix)
    // =========================================================================

    /**
     * Shared row-level authorization helper — verifies that the authenticated user
     * has the right to act on a specific trip.
     *
     * <p><strong>Gap #3 fix:</strong> The original LLD never verified that the
     * authenticated driver is the one actually assigned to the trip. This method
     * implements the HLD's promise that "caller's ID matches the driver assigned
     * to the trip".</p>
     *
     * <p>Authorization rules:
     * <ul>
     *   <li>FLEET_MANAGER: Always allowed (oversight privilege, no ownership check)</li>
     *   <li>DRIVER: Only allowed if they are the driver assigned to the trip</li>
     *   <li>Other roles: Always denied for trip actions</li>
     * </ul>
     * </p>
     *
     * @param trip        the trip to check authorization for
     * @param currentUser the authenticated user attempting the action
     * @throws AccessDeniedException if the user is not permitted to act on this trip
     */
    private void assertCallerCanActOnTrip(Trip trip, AuthUser currentUser) {
        // Fleet Managers have oversight privilege — no ownership check needed
        if (currentUser.hasRole("FLEET_MANAGER")) {
            return;
        }

        // Drivers can only act on their own trips
        if (currentUser.hasRole("DRIVER")) {
            boolean isOwner = trip.getDriver().getUser().getId().equals(currentUser.getId());
            if (!isOwner) {
                throw new AccessDeniedException("You are not the driver assigned to this trip.");
            }
            return;
        }

        // All other roles are denied
        throw new AccessDeniedException("Role not permitted to act on trips.");
    }


    // =========================================================================
    //  STEP 5.6 — Checkpoint Creation (gap #11 fix)
    // =========================================================================

    /**
     * Adds ordered waypoints (checkpoints) to a trip.
     *
     * <p><strong>Gap #11 fix:</strong> The refuel-at-checkpoint flow assumes a
     * checkpoint already exists, but nothing in the original design creates one.
     * This method provides the missing creation step.</p>
     *
     * <p>Checkpoints can be added while a trip is in DRAFT or DISPATCHED status
     * (adding a stop mid-route is realistic). Adding checkpoints to COMPLETED or
     * CANCELLED trips is blocked.</p>
     *
     * @param tripId      the ID of the trip to add checkpoints to
     * @param dtos        list of checkpoint creation payloads
     * @param currentUser the authenticated user adding checkpoints
     * @return list of created checkpoint response DTOs
     */
    @Transactional
    public List<CheckpointResponseDTO> addCheckpoints(Long tripId, List<CheckpointCreateDTO> dtos,
                                                      AuthUser currentUser) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));

        // State guard: only DRAFT or DISPATCHED trips can receive new checkpoints
        String status = trip.getStatus();
        if (!TripStatus.DRAFT.name().equals(status) && !TripStatus.DISPATCHED.name().equals(status)) {
            throw new IllegalStateException(
                    "Checkpoints can only be added to DRAFT or DISPATCHED trips. Current status: " + status);
        }

        // Row-level authorization
        assertCallerCanActOnTrip(trip, currentUser);

        // Create checkpoint entities from DTOs
        List<TripCheckpoint> checkpoints = new ArrayList<>();
        for (CheckpointCreateDTO dto : dtos) {
            TripCheckpoint checkpoint = new TripCheckpoint();
            checkpoint.setTrip(trip);
            checkpoint.setName(dto.getName());
            checkpoint.setCheckpointOrder(dto.getCheckpointOrder());
            checkpoint.setExpectedArrival(dto.getExpectedArrival());
            checkpoint.setStatus("PENDING");  // New checkpoints always start as PENDING
            checkpoints.add(checkpoint);
        }

        List<TripCheckpoint> saved = checkpointRepository.saveAll(checkpoints);
        return saved.stream()
                .map(CheckpointResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    // =========================================================================
    //  Refuel at Checkpoint (logCheckpointRefuel — with gap #3 auth fix)
    // =========================================================================

    /**
     * Logs a refuel event at a specific trip checkpoint.
     *
     * <p>This method:
     * <ol>
     *   <li>Verifies the trip is DISPATCHED</li>
     *   <li>Applies row-level authorization (gap #3 fix)</li>
     *   <li>Validates the checkpoint belongs to the trip and is PENDING</li>
     *   <li>Creates a FuelLog record linked to the trip and vehicle</li>
     *   <li>Marks the checkpoint as ARRIVED with odometer and timestamp</li>
     * </ol>
     * </p>
     *
     * @param tripId      the ID of the active trip
     * @param dto         refuel details including checkpoint ID, fuel, cost, odometer
     * @param currentUser the authenticated user logging the refuel
     */
    @Transactional
    public void logCheckpointRefuel(Long tripId, RefuelAtCheckpointDTO dto, AuthUser currentUser) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));

        // State guard: refuelling only makes sense for an active trip
        if (!TripStatus.DISPATCHED.name().equals(trip.getStatus())) {
            throw new IllegalStateException(
                    "Refuelling can only be logged for DISPATCHED trips. Current status: " + trip.getStatus());
        }

        // Row-level authorization (gap #3 fix — HLD promised this but it was never implemented)
        assertCallerCanActOnTrip(trip, currentUser);

        // Validate the checkpoint exists and belongs to this trip
        TripCheckpoint checkpoint = checkpointRepository.findById(dto.getCheckpointId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Checkpoint not found with ID: " + dto.getCheckpointId()));

        if (!checkpoint.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException("Checkpoint does not belong to this trip.");
        }

        if (!"PENDING".equals(checkpoint.getStatus())) {
            throw new IllegalStateException(
                    "Checkpoint has already been reached (status: " + checkpoint.getStatus() + ").");
        }

        // Resolve the logged-by user from the authenticated principal
        User loggedByUser = currentUser.getUser();

        // Create the FuelLog entry
        FuelLog fuelLog = new FuelLog();
        fuelLog.setVehicle(trip.getVehicle());
        fuelLog.setTrip(trip);
        fuelLog.setLoggedBy(loggedByUser);
        fuelLog.setFuelQuantity(dto.getFuelQuantity());
        fuelLog.setTotalCost(dto.getTotalCost());
        fuelLog.setOdometerReading(dto.getOdometerReading());
        fuelLog.setRefuelDate(LocalDateTime.now());
        fuelLog.setProofImageUrl(dto.getProofImageUrl());
        fuelLogRepository.save(fuelLog);

        // Mark checkpoint as ARRIVED
        checkpoint.setStatus("ARRIVED");
        checkpoint.setReachedAt(LocalDateTime.now());
        checkpoint.setOdometerReadingOnArrival(dto.getOdometerReading());
        checkpointRepository.save(checkpoint);

        log.info("Refuel logged at checkpoint {} (trip {}) by user {}. Fuel: {}, Cost: {}",
                dto.getCheckpointId(), tripId, currentUser.getId(),
                dto.getFuelQuantity(), dto.getTotalCost());
    }


    // =========================================================================
    //  READ — List Trips (paginated)
    // =========================================================================

    /**
     * Returns a paginated list of trips with optional filtering by status and driver.
     *
     * @param driverId optional driver ID filter
     * @param status   optional status filter
     * @param pageable pagination and sorting parameters
     * @return page of trip response DTOs
     */
    @Transactional(readOnly = true)
    public Page<TripResponseDTO> listTrips(Long driverId, String status, Pageable pageable) {
        Page<Trip> trips;

        if (driverId != null && status != null) {
            trips = tripRepository.findByDriverIdAndStatus(driverId, status, pageable);
        } else if (driverId != null) {
            trips = tripRepository.findByDriverId(driverId, pageable);
        } else if (status != null) {
            trips = tripRepository.findByStatus(status, pageable);
        } else {
            trips = tripRepository.findAll(pageable);
        }

        return trips.map(TripResponseDTO::fromEntity);
    }


    // =========================================================================
    //  READ — Get Single Trip
    // =========================================================================

    /**
     * Retrieves a single trip by its ID.
     *
     * @param tripId the trip ID
     * @return response DTO of the found trip
     */
    @Transactional(readOnly = true)
    public TripResponseDTO getById(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));
        return TripResponseDTO.fromEntity(trip);
    }


    // =========================================================================
    //  READ — Get Checkpoints for a Trip
    // =========================================================================

    /**
     * Retrieves all checkpoints for a trip, ordered by checkpoint_order.
     *
     * @param tripId the trip ID
     * @return ordered list of checkpoint response DTOs
     */
    @Transactional(readOnly = true)
    public List<CheckpointResponseDTO> getCheckpoints(Long tripId) {
        // Verify trip exists
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with ID: " + tripId);
        }

        return checkpointRepository.findByTripIdOrderByCheckpointOrderAsc(tripId)
                .stream()
                .map(CheckpointResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
