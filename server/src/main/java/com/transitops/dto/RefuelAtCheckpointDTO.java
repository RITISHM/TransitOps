package com.transitops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for logging a refuel event at a specific trip checkpoint.
 *
 * <p>This DTO captures the fuel and cost details along with the odometer reading
 * at the checkpoint. The service layer will also mark the referenced checkpoint
 * as ARRIVED and record the arrival timestamp.</p>
 */
public class RefuelAtCheckpointDTO {

    /** ID of the checkpoint where refuelling occurred */
    @NotNull(message = "Checkpoint ID is required")
    private Long checkpointId;

    /** Amount of fuel added (in litres) */
    @NotNull(message = "Fuel quantity is required")
    @DecimalMin(value = "0.01", message = "Fuel quantity must be greater than zero")
    private BigDecimal fuelQuantity;

    /** Total cost of the refuel */
    @NotNull(message = "Total cost is required")
    @DecimalMin(value = "0.01", message = "Total cost must be greater than zero")
    private BigDecimal totalCost;

    /** Odometer reading at the time of refuelling */
    @NotNull(message = "Odometer reading is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Odometer reading cannot be negative")
    private BigDecimal odometerReading;

    /** Optional URL to a receipt/proof image */
    private String proofImageUrl;


    public RefuelAtCheckpointDTO() {
    }

    // ----- Getters -----

    public Long getCheckpointId() { return checkpointId; }
    public BigDecimal getFuelQuantity() { return fuelQuantity; }
    public BigDecimal getTotalCost() { return totalCost; }
    public BigDecimal getOdometerReading() { return odometerReading; }
    public String getProofImageUrl() { return proofImageUrl; }

    // ----- Setters -----

    public void setCheckpointId(Long checkpointId) { this.checkpointId = checkpointId; }
    public void setFuelQuantity(BigDecimal fuelQuantity) { this.fuelQuantity = fuelQuantity; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public void setOdometerReading(BigDecimal odometerReading) { this.odometerReading = odometerReading; }
    public void setProofImageUrl(String proofImageUrl) { this.proofImageUrl = proofImageUrl; }
}
