package com.transitops.repository;

import com.transitops.domain.TripCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripCheckpointRepository extends JpaRepository<TripCheckpoint, Long> {
}
