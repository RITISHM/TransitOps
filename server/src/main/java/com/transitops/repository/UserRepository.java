package com.transitops.repository;

import com.transitops.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address — used for authentication (login).
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks whether a user with the given email already exists.
     * Used to produce a friendly 409 Conflict during driver creation
     * instead of a raw DB unique constraint error.
     */
    boolean existsByEmail(String email);
}

