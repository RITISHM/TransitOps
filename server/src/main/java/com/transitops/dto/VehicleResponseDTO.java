package com.transitops.dto;

import com.transitops.domain.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning Vehicle data in API responses.
 *
 * <p>Flattens the {@link Vehicle} entity to avoid lazy-loading issues — the
 * associated Region is represented by its ID and name rather than a nested
 * object reference. This keeps JSON serialization predictable regardless of
 * the Hibernate session state.</p>
 */
public class VehicleResponseDTO {

    private Long id;
    private String registrationNumber;
    private String vehicleName;
    private String vehicleType;
    private String fuelType;
    private BigDecimal maxLoadCapacity;
    private BigDecimal currentOdometer;
    private BigDecimal acquisitionCost;
    private LocalDate acquisitionDate;
    private Long regionId;
    private String regionName;
    private String status;
    private LocalDate nextMaintenanceDueDate;
    private BigDecimal nextMaintenanceDueOdometer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public VehicleResponseDTO() {
    }

    /**
     * Factory method that converts a Vehicle entity into a response DTO.
     *
     * <p>This approach keeps the mapping logic in one place and avoids scattering
     * entity-to-DTO conversion across service and controller layers.</p>
     *
     * @param vehicle the JPA entity to convert
     * @return a fully populated response DTO
     */
    public static VehicleResponseDTO fromEntity(Vehicle vehicle) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId(vehicle.getId());
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());
        dto.setVehicleName(vehicle.getVehicleName());
        dto.setVehicleType(vehicle.getVehicleType());
        dto.setFuelType(vehicle.getFuelType());
        dto.setMaxLoadCapacity(vehicle.getMaxLoadCapacity());
        dto.setCurrentOdometer(vehicle.getCurrentOdometer());
        dto.setAcquisitionCost(vehicle.getAcquisitionCost());
        dto.setAcquisitionDate(vehicle.getAcquisitionDate());
        dto.setStatus(vehicle.getStatus());
        dto.setNextMaintenanceDueDate(vehicle.getNextMaintenanceDueDate());
        dto.setNextMaintenanceDueOdometer(vehicle.getNextMaintenanceDueOdometer());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setUpdatedAt(vehicle.getUpdatedAt());

        // Flatten the Region relationship to avoid lazy-loading pitfalls
        if (vehicle.getRegion() != null) {
            dto.setRegionId(vehicle.getRegion().getId());
            dto.setRegionName(vehicle.getRegion().getName());
        }

        return dto;
    }


    // ----- Getters -----

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public BigDecimal getMaxLoadCapacity() {
        return maxLoadCapacity;
    }

    public BigDecimal getCurrentOdometer() {
        return currentOdometer;
    }

    public BigDecimal getAcquisitionCost() {
        return acquisitionCost;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public Long getRegionId() {
        return regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getNextMaintenanceDueDate() {
        return nextMaintenanceDueDate;
    }

    public BigDecimal getNextMaintenanceDueOdometer() {
        return nextMaintenanceDueOdometer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ----- Setters -----

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setMaxLoadCapacity(BigDecimal maxLoadCapacity) {
        this.maxLoadCapacity = maxLoadCapacity;
    }

    public void setCurrentOdometer(BigDecimal currentOdometer) {
        this.currentOdometer = currentOdometer;
    }

    public void setAcquisitionCost(BigDecimal acquisitionCost) {
        this.acquisitionCost = acquisitionCost;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNextMaintenanceDueDate(LocalDate nextMaintenanceDueDate) {
        this.nextMaintenanceDueDate = nextMaintenanceDueDate;
    }

    public void setNextMaintenanceDueOdometer(BigDecimal nextMaintenanceDueOdometer) {
        this.nextMaintenanceDueOdometer = nextMaintenanceDueOdometer;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
