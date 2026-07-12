package com.transitops.service;

import com.transitops.domain.Region;
import com.transitops.domain.Vehicle;
import com.transitops.dto.VehicleCreateDTO;
import com.transitops.dto.VehicleResponseDTO;
import com.transitops.enums.VehicleStatus;
import com.transitops.exception.DuplicateResourceException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.RegionRepository;
import com.transitops.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for Vehicle management operations.
 *
 * <p>Implements the business rules specified in Phase 4 of the gap analysis:
 * <ul>
 *   <li>Full CRUD gated to FLEET_MANAGER role (enforced at controller level)</li>
 *   <li>Uniqueness enforcement on registration number with friendly 409 errors</li>
 *   <li>Dynamic filtering by vehicleType, status, and regionId using JPA Specifications</li>
 *   <li>Retirement guard: cannot retire a vehicle that is currently ON_TRIP</li>
 * </ul>
 * </p>
 */
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RegionRepository regionRepository;

    /**
     * Constructs a VehicleService with the required repository dependencies.
     *
     * @param vehicleRepository repository for Vehicle CRUD operations
     * @param regionRepository  repository for Region lookups (validating regionId)
     */
    public VehicleService(VehicleRepository vehicleRepository, RegionRepository regionRepository) {
        this.vehicleRepository = vehicleRepository;
        this.regionRepository = regionRepository;
    }


    // =========================================================================
    //  CREATE
    // =========================================================================

    /**
     * Creates a new vehicle in the system.
     *
     * <p>Business rules enforced:
     * <ol>
     *   <li>Registration number must be unique — throws {@link DuplicateResourceException} (409)</li>
     *   <li>Referenced region must exist — throws {@link ResourceNotFoundException} (404)</li>
     *   <li>New vehicles always start with status AVAILABLE and odometer at zero</li>
     * </ol>
     * </p>
     *
     * @param dto the validated vehicle creation payload
     * @return response DTO of the newly created vehicle
     */
    @Transactional
    public VehicleResponseDTO create(VehicleCreateDTO dto) {
        // Guard: duplicate registration number → 409 (not a raw SQL exception)
        if (vehicleRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "A vehicle with registration number '" + dto.getRegistrationNumber() + "' already exists.");
        }

        // Validate that the referenced region exists
        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Region not found with ID: " + dto.getRegionId()));

        // Map DTO fields onto a new Vehicle entity
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setVehicleType(dto.getVehicleType().name());   // Enum → String for DB column
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setMaxLoadCapacity(dto.getMaxLoadCapacity());
        vehicle.setAcquisitionCost(dto.getAcquisitionCost());
        vehicle.setAcquisitionDate(dto.getAcquisitionDate());
        vehicle.setRegion(region);
        vehicle.setStatus(VehicleStatus.AVAILABLE.name());     // New vehicles start as AVAILABLE

        Vehicle saved = vehicleRepository.save(vehicle);
        return VehicleResponseDTO.fromEntity(saved);
    }


    // =========================================================================
    //  READ — LIST (paginated + filtered)
    // =========================================================================

    /**
     * Returns a paginated, optionally filtered list of vehicles.
     *
     * <p>Supports the dashboard requirement (PDF §3.2) to filter vehicles by type,
     * status, and region. Uses JPA Specifications so filters compose dynamically —
     * only non-null filter parameters contribute to the WHERE clause.</p>
     *
     * @param vehicleType optional filter — matches vehicles.vehicle_type exactly
     * @param status      optional filter — matches vehicles.status exactly
     * @param regionId    optional filter — matches vehicles.region_id exactly
     * @param pageable    pagination and sorting parameters
     * @return page of vehicle response DTOs
     */
    @Transactional(readOnly = true)
    public Page<VehicleResponseDTO> list(String vehicleType, String status, Long regionId, Pageable pageable) {
        Specification<Vehicle> spec = Specification.where(null);

        if (vehicleType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("vehicleType"), vehicleType));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (regionId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("region").get("id"), regionId));
        }

        return vehicleRepository.findAll(spec, pageable).map(VehicleResponseDTO::fromEntity);
    }


    // =========================================================================
    //  READ — SINGLE
    // =========================================================================

    /**
     * Retrieves a single vehicle by its ID.
     *
     * @param id the vehicle ID
     * @return response DTO of the found vehicle
     * @throws ResourceNotFoundException if no vehicle exists with that ID
     */
    @Transactional(readOnly = true)
    public VehicleResponseDTO getById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        return VehicleResponseDTO.fromEntity(vehicle);
    }


    // =========================================================================
    //  UPDATE
    // =========================================================================

    /**
     * Updates an existing vehicle's fields.
     *
     * <p>All fields from the DTO overwrite the existing values. The registration
     * number uniqueness check accounts for the case where the vehicle keeps its
     * own current registration number (i.e., only flags a conflict if a
     * <em>different</em> vehicle already uses that number).</p>
     *
     * @param id  the ID of the vehicle to update
     * @param dto the validated update payload
     * @return response DTO of the updated vehicle
     */
    @Transactional
    public VehicleResponseDTO update(Long id, VehicleCreateDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

        // Guard: block updates on vehicles that are currently on a trip
        if (VehicleStatus.ON_TRIP.name().equals(vehicle.getStatus())) {
            throw new IllegalStateException(
                    "Cannot update a vehicle that is currently ON_TRIP. Complete or cancel the active trip first.");
        }

        // Guard: registration number uniqueness — allow keeping the current one
        if (!vehicle.getRegistrationNumber().equals(dto.getRegistrationNumber())
                && vehicleRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "A vehicle with registration number '" + dto.getRegistrationNumber() + "' already exists.");
        }

        // Validate that the referenced region exists
        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Region not found with ID: " + dto.getRegionId()));

        // Apply updates
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setVehicleType(dto.getVehicleType().name());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setMaxLoadCapacity(dto.getMaxLoadCapacity());
        vehicle.setAcquisitionCost(dto.getAcquisitionCost());
        vehicle.setAcquisitionDate(dto.getAcquisitionDate());
        vehicle.setRegion(region);

        Vehicle saved = vehicleRepository.save(vehicle);
        return VehicleResponseDTO.fromEntity(saved);
    }


    // =========================================================================
    //  RETIRE
    // =========================================================================

    /**
     * Retires a vehicle, permanently removing it from the active fleet.
     *
     * <p>A retired vehicle is excluded from fleet utilization calculations and
     * cannot be assigned to new trips. This action is blocked if the vehicle is
     * currently {@code ON_TRIP} — the active trip must be completed or cancelled
     * first.</p>
     *
     * @param id the ID of the vehicle to retire
     */
    @Transactional
    public void retire(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

        // Guard: cannot retire a vehicle that is actively on a trip
        if (VehicleStatus.ON_TRIP.name().equals(vehicle.getStatus())) {
            throw new IllegalStateException(
                    "Cannot retire a vehicle that is currently ON_TRIP. Complete or cancel the active trip first.");
        }

        // Guard: idempotency — retiring an already-retired vehicle is a no-op
        if (VehicleStatus.RETIRED.name().equals(vehicle.getStatus())) {
            return;
        }

        vehicle.setStatus(VehicleStatus.RETIRED.name());
        vehicleRepository.save(vehicle);
    }
}
