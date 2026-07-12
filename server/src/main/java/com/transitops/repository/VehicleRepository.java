package com.transitops.repository;

import com.transitops.domain.Vehicle;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository interface for managing Vehicle entities.
 *
 * <p>Extends {@link JpaSpecificationExecutor} to support dynamic, criteria-based
 * filtering in the list endpoint (filter by vehicleType, status, regionId).</p>
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    /**
     * Acquires a pessimistic write lock on the vehicle row — used by the Trip
     * state machine to prevent concurrent dispatch of the same vehicle.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Vehicle v WHERE v.id = :id")
    Optional<Vehicle> findByIdForUpdate(@Param("id") Long id);

    /**
     * Checks whether a vehicle with the given registration number already exists.
     * Used to produce a friendly 409 Conflict instead of a raw DB constraint error.
     */
    boolean existsByRegistrationNumber(String registrationNumber);

    /**
     * Finds all vehicles with the given operational status.
     */
    List<Vehicle> findByStatus(String status);
}

