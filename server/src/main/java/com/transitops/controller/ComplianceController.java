package com.transitops.controller;

import com.transitops.domain.Driver;
import com.transitops.repository.DriverRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for Compliance and Safety Officer queries.
 */
@RestController
@RequestMapping("/api/compliance")
public class ComplianceController {

    private final DriverRepository driverRepository;

    public ComplianceController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    /**
     * Lists drivers whose licenses are expiring within a certain number of days.
     */
    @GetMapping("/expiring-licenses")
    @PreAuthorize("hasRole('SAFETY_OFFICER')")
    public List<Driver> expiringLicenses(@RequestParam(defaultValue = "30") int withinDays) {
        return driverRepository.findByLicenseExpiryDateBefore(LocalDate.now().plusDays(withinDays));
    }

    /**
     * Lists all suspended drivers.
     */
    @GetMapping("/suspended-drivers")
    @PreAuthorize("hasRole('SAFETY_OFFICER')")
    public List<Driver> suspendedDrivers() {
        return driverRepository.findByStatus("SUSPENDED");
    }
}
