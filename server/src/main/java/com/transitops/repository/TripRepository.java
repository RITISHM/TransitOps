package com.transitops.repository;

import com.transitops.domain.Trip;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository interface for managing Trip entities.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     * Acquires a pessimistic write lock on the trip row — used during state
     * transitions (dispatch, complete, cancel) to prevent concurrent modifications.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Trip t WHERE t.id = :id")
    Optional<Trip> findByIdForUpdate(@Param("id") Long id);

    /**
     * Finds all trips for a specific driver — used by drivers to view their own trips.
     */
    Page<Trip> findByDriverId(Long driverId, Pageable pageable);

    /**
     * Finds trips filtered by status.
     */
    Page<Trip> findByStatus(String status, Pageable pageable);

    /**
     * Finds trips filtered by both driver and status.
     */
    Page<Trip> findByDriverIdAndStatus(Long driverId, String status, Pageable pageable);

    /**
     * Counts trips by status — used for dashboard KPIs.
     */
    long countByStatus(String status);
}

