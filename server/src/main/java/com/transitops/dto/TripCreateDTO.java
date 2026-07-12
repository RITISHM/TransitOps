package com.transitops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating a new trip draft.
 *
 * <p>The driver specifies the vehicle, route, and cargo details. The system will
 * validate asset availability and compliance at draft-creation time, and re-validate
 * at dispatch time (gap #2 fix).</p>
 */
public class TripCreateDTO {

    /** ID of the vehicle to be assigned to this trip */
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    /** ID of the driver to be assigned — may differ from the authenticated user */
    @NotNull(message = "Driver ID is required")
    private Long driverId;

    /** Trip origin location */
    @NotBlank(message = "Source is required")
    private String source;

    /** Trip destination location */
    @NotBlank(message = "Destination is required")
    private String destination;

    /** Estimated distance in kilometers */
    private BigDecimal expectedDistance;

    /** Weight of cargo to be carried — validated against vehicle's max load capacity */
    @NotNull(message = "Cargo weight is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Cargo weight cannot be negative")
    private BigDecimal cargoWeight;

    /** Expected revenue for this trip */
    private BigDecimal revenue;


    public TripCreateDTO() {
    }

    // ----- Getters -----

    public Long getVehicleId() {
        return vehicleId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public BigDecimal getExpectedDistance() {
        return expectedDistance;
    }

    public BigDecimal getCargoWeight() {
        return cargoWeight;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    // ----- Setters -----

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setExpectedDistance(BigDecimal expectedDistance) {
        this.expectedDistance = expectedDistance;
    }

    public void setCargoWeight(BigDecimal cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
