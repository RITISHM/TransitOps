package com.transitops.repository;

import com.transitops.domain.TripCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository interface for managing TripCheckpoint entities.
 */
@Repository
public interface TripCheckpointRepository extends JpaRepository<TripCheckpoint, Long> {

    /**
     * Retrieves all checkpoints for a trip, ordered by their sequence number.
     * Used to display the route's waypoints in correct order.
     */
    List<TripCheckpoint> findByTripIdOrderByCheckpointOrderAsc(Long tripId);
}

