package com.transitops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for opening a new maintenance ticket.
 */
public class MaintenanceCreateDTO {

    /** ID of the vehicle undergoing maintenance */
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    /** Type of maintenance (e.g., "PREVENTIVE", "REPAIR") */
    @NotBlank(message = "Maintenance type is required")
    private String maintenanceType;

    /** Description of the issue or service required */
    @NotBlank(message = "Description is required")
    private String description;

    /** Estimated cost of the maintenance */
    @DecimalMin(value = "0.00", inclusive = true, message = "Expected cost cannot be negative")
    private BigDecimal expectedCost;

    public MaintenanceCreateDTO() {}

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getExpectedCost() {
        return expectedCost;
    }

    public void setExpectedCost(BigDecimal expectedCost) {
        this.expectedCost = expectedCost;
    }
}
