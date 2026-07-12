package com.transitops.service;

import com.transitops.domain.Driver;
import com.transitops.domain.User;
import com.transitops.dto.DriverCreateDTO;
import com.transitops.dto.DriverResponseDTO;
import com.transitops.dto.DriverUpdateDTO;
import com.transitops.enums.DriverStatus;
import com.transitops.exception.DuplicateResourceException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.DriverRepository;
import com.transitops.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service layer for Driver management operations.
 *
 * <p>Implements the business rules specified in Phase 4, Step 4.2:
 * <ul>
 *   <li>Transactional creation of both User and Driver entities atomically</li>
 *   <li>Uniqueness enforcement on email and license number with friendly 409 errors</li>
 *   <li>License expiry validation at creation time (not just at dispatch)</li>
 *   <li>Safety score update stub for Phase 9 (SAFETY_OFFICER only)</li>
 * </ul>
 * </p>
 */
@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a DriverService with the required dependencies.
     *
     * @param driverRepository repository for Driver CRUD operations
     * @param userRepository   repository for User CRUD operations
     * @param passwordEncoder  BCrypt encoder for hashing initial passwords
     */
    public DriverService(DriverRepository driverRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================================================================
    //  CREATE — Atomic User + Driver creation
    // =========================================================================

    /**
     * Creates a new Driver along with its linked User account in a single
     * transactional boundary.
     *
     * <p>This ensures that a failure at any point never leaves an orphaned User
     * without a Driver (or vice versa). The transaction rolls back entirely if
     * any step fails.</p>
     *
     * <p>Business rules enforced:
     * <ol>
     *   <li>Email must be unique — throws {@link DuplicateResourceException} (409)</li>
     *   <li>License number must be unique — throws {@link DuplicateResourceException} (409)</li>
     *   <li>License expiry in the past is caught by {@code @Future} bean validation (400)</li>
     *   <li>Password is BCrypt-hashed before storage</li>
     *   <li>New drivers start with status AVAILABLE and safety score 100.00</li>
     * </ol>
     * </p>
     *
     * @param dto the validated driver creation payload (combines user + driver fields)
     * @return response DTO of the newly created driver with flattened user details
     */
    @Transactional
    public DriverResponseDTO create(DriverCreateDTO dto) {
        // Guard: duplicate email → 409
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                    "A user with email '" + dto.getEmail() + "' already exists.");
        }

        // Guard: duplicate license number → 409
        if (driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new DuplicateResourceException(
                    "A driver with license number '" + dto.getLicenseNumber() + "' already exists.");
        }

        // Step 1: Create the User entity (role = DRIVER, password BCrypt-hashed)
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole("DRIVER");
        user.setIsActive(true);
        User savedUser = userRepository.save(user);

        // Step 2: Create the Driver entity linked to the User
        Driver driver = new Driver();
        driver.setUser(savedUser);
        driver.setDob(dto.getDob());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setLicenseCategory(dto.getLicenseCategory());
        driver.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        driver.setContactNumber(dto.getContactNumber());
        driver.setSafetyScore(new BigDecimal("100.00"));         // Default safety score
        driver.setStatus(DriverStatus.AVAILABLE.name());         // New drivers start as AVAILABLE

        Driver savedDriver = driverRepository.save(driver);
        return DriverResponseDTO.fromEntity(savedDriver);
    }


    // =========================================================================
    //  READ — LIST (paginated + filtered)
    // =========================================================================

    /**
     * Returns a paginated, optionally filtered list of drivers.
     *
     * <p>Eagerly fetches the linked User entity to avoid N+1 queries when
     * building response DTOs.</p>
     *
     * @param status   optional filter — matches drivers.status exactly
     * @param pageable pagination and sorting parameters
     * @return page of driver response DTOs
     */
    @Transactional(readOnly = true)
    public Page<DriverResponseDTO> list(String status, Pageable pageable) {
        Specification<Driver> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        // Eagerly join the User association to prevent lazy-loading issues
        Specification<Driver> fetchUser = (root, query, cb) -> {
            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("user", jakarta.persistence.criteria.JoinType.LEFT);
            }
            return cb.conjunction();
        };

        spec = spec.and(fetchUser);

        return driverRepository.findAll(spec, pageable).map(DriverResponseDTO::fromEntity);
    }


    // =========================================================================
    //  READ — SINGLE
    // =========================================================================

    /**
     * Retrieves a single driver by their ID.
     *
     * @param id the driver ID
     * @return response DTO of the found driver
     * @throws ResourceNotFoundException if no driver exists with that ID
     */
    @Transactional(readOnly = true)
    public DriverResponseDTO getById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + id));
        return DriverResponseDTO.fromEntity(driver);
    }


    // =========================================================================
    //  UPDATE
    // =========================================================================

    /**
     * Updates an existing driver's mutable operational fields.
     *
     * <p>Only non-null fields from the DTO are applied. User-level fields (name,
     * email, password) are not modifiable through this endpoint — they require a
     * separate user-administration flow.</p>
     *
     * @param id  the ID of the driver to update
     * @param dto the update payload (all fields optional)
     * @return response DTO of the updated driver
     */
    @Transactional
    public DriverResponseDTO update(Long id, DriverUpdateDTO dto) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + id));

        // Apply only non-null fields from the update DTO
        if (dto.getLicenseNumber() != null) {
            // Guard: uniqueness check — allow keeping the current license number
            if (!driver.getLicenseNumber().equals(dto.getLicenseNumber())
                    && driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
                throw new DuplicateResourceException(
                        "A driver with license number '" + dto.getLicenseNumber() + "' already exists.");
            }
            driver.setLicenseNumber(dto.getLicenseNumber());
        }

        if (dto.getLicenseCategory() != null) {
            driver.setLicenseCategory(dto.getLicenseCategory());
        }

        if (dto.getLicenseExpiryDate() != null) {
            driver.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        }

        if (dto.getContactNumber() != null) {
            driver.setContactNumber(dto.getContactNumber());
        }

        if (dto.getDob() != null) {
            driver.setDob(dto.getDob());
        }

        if (dto.getStatus() != null) {
            // Validate that the status string is a valid DriverStatus enum value
            try {
                DriverStatus.valueOf(dto.getStatus());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Invalid driver status: '" + dto.getStatus()
                                + "'. Must be one of: AVAILABLE, ON_TRIP, OFF_DUTY, SUSPENDED.");
            }
            driver.setStatus(dto.getStatus());
        }

        Driver saved = driverRepository.save(driver);
        return DriverResponseDTO.fromEntity(saved);
    }


    // =========================================================================
    //  SAFETY SCORE — Stub for Phase 9
    // =========================================================================

    /**
     * Updates a driver's safety score.
     *
     * <p>This is a stub created in Phase 4 (Step 4.2) as a dependency for Phase 9.
     * The route is gated to {@code SAFETY_OFFICER} role at the controller level.
     * Full scoring logic (incident-based calculation, threshold alerts) will be
     * implemented in Phase 9.</p>
     *
     * @param id         the driver ID
     * @param safetyScore the new safety score value (0.00 – 100.00)
     * @return response DTO of the updated driver
     */
    @Transactional
    public DriverResponseDTO updateSafetyScore(Long id, BigDecimal safetyScore) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + id));

        // Basic range validation
        if (safetyScore.compareTo(BigDecimal.ZERO) < 0
                || safetyScore.compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException("Safety score must be between 0.00 and 100.00.");
        }

        driver.setSafetyScore(safetyScore);
        Driver saved = driverRepository.save(driver);
        return DriverResponseDTO.fromEntity(saved);
    }
}
