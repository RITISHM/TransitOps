package com.transitops.repository;

import com.transitops.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository interface for managing Region entities.
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}
