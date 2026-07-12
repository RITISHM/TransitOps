package com.transitops.repository;

import com.transitops.domain.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository interface for managing FuelLog entities.
 */
@Repository
public interface FuelLogRepository extends JpaRepository<FuelLog, Long> {
}
