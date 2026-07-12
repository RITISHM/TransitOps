package com.transitops.service;

import com.transitops.domain.MaintenanceLog;
import com.transitops.domain.User;
import com.transitops.domain.Vehicle;
import com.transitops.dto.MaintenanceCloseDTO;
import com.transitops.dto.MaintenanceCreateDTO;
import com.transitops.enums.VehicleStatus;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.MaintenanceLogRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.security.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service for managing vehicle maintenance operations.
 */
@Service
public class MaintenanceService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final VehicleRepository vehicleRepository;

    public MaintenanceService(MaintenanceLogRepository maintenanceLogRepository,
                              VehicleRepository vehicleRepository) {
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Opens a new maintenance ticket and locks the vehicle (sets to IN_SHOP).
     */
    @Transactional
    public MaintenanceLog openMaintenanceTicket(MaintenanceCreateDTO dto, AuthUser currentUser) {
        Vehicle vehicle = vehicleRepository.findByIdForUpdate(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (!VehicleStatus.AVAILABLE.name().equals(vehicle.getStatus())) {
            throw new IllegalStateException("Vehicle must be AVAILABLE to enter maintenance. Current status: " + vehicle.getStatus());
        }

        vehicle.setStatus(VehicleStatus.IN_SHOP.name());
        vehicleRepository.save(vehicle);

        MaintenanceLog log = new MaintenanceLog();
        log.setVehicle(vehicle);
        log.setLoggedBy(currentUser.getUser()); // Resolves gap #15
        log.setMaintenanceType(dto.getMaintenanceType());
        log.setDescription(dto.getDescription());
        log.setExpectedCost(dto.getExpectedCost());
        log.setStartDate(LocalDate.now());
        log.setStatus("OPEN");

        return maintenanceLogRepository.save(log);
    }

    /**
     * Closes an active maintenance ticket and releases the vehicle (if not retired).
     * Updates upcoming maintenance tracking (fixes gap #13).
     */
    @Transactional
    public void closeMaintenanceTicket(Long ticketId, MaintenanceCloseDTO dto) {
        MaintenanceLog log = maintenanceLogRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance ticket not found"));

        if (!"OPEN".equals(log.getStatus())) {
            throw new IllegalStateException("Maintenance ticket is already " + log.getStatus());
        }

        Vehicle vehicle = vehicleRepository.findByIdForUpdate(log.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        log.setFinalCost(dto.getFinalCost());
        log.setEndDate(LocalDate.now());
        log.setStatus("CLOSED");
        maintenanceLogRepository.save(log);

        if (dto.getNextServiceDueDate() != null) {
            vehicle.setNextMaintenanceDueDate(dto.getNextServiceDueDate());
        }
        if (dto.getNextServiceDueOdometer() != null) {
            vehicle.setNextMaintenanceDueOdometer(dto.getNextServiceDueOdometer());
        }

        if (!VehicleStatus.RETIRED.name().equals(vehicle.getStatus())) {
            vehicle.setStatus(VehicleStatus.AVAILABLE.name());
        }

        vehicleRepository.save(vehicle);
    }
}
