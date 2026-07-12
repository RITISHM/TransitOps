package com.transitops.repository;

import com.transitops.domain.Driver;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository interface for managing Driver entities.
 *
 * <p>Extends {@link JpaSpecificationExecutor} to support dynamic, criteria-based
 * filtering in the list endpoint (filter by status, license expiry, etc.).</p>
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {

    /**
     * Acquires a pessimistic write lock on the driver row — used by the Trip
     * state machine to prevent concurrent dispatch of the same driver.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Driver d WHERE d.id = :id")
    Optional<Driver> findByIdForUpdate(@Param("id") Long id);

    /**
     * Finds drivers whose license expires before the given date — used by the
     * compliance notification scheduler (Phase 9).
     */
    List<Driver> findByLicenseExpiryDateBefore(LocalDate date);

    /**
     * Finds all drivers with the given operational status.
     */
    List<Driver> findByStatus(String status);

    /**
     * Checks whether a driver with the given license number already exists.
     * Used to produce a friendly 409 Conflict instead of a raw DB constraint error.
     */
    boolean existsByLicenseNumber(String licenseNumber);

    /**
     * Finds the Driver record linked to a specific User ID.
     * Useful for resolving the authenticated user's driver profile.
     */
    Optional<Driver> findByUserId(Long userId);

    /**
     * Counts drivers whose status is in the given list.
     * Used for the Dashboard "Drivers On Duty" KPI.
     */
    long countByStatusIn(List<String> statuses);
}

