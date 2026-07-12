package com.transitops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for completing a trip.
 *
 * <p>The driver provides the final odometer reading, which is validated to be
 * greater than or equal to the start odometer captured at dispatch time.</p>
 */
public class TripCompleteDTO {

    /** Final odometer reading when the trip ends — must be ≥ start odometer */
    @NotNull(message = "End odometer reading is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "End odometer cannot be negative")
    private BigDecimal endOdometer;


    public TripCompleteDTO() {
    }

    public BigDecimal getEndOdometer() {
        return endOdometer;
    }

    public void setEndOdometer(BigDecimal endOdometer) {
        this.endOdometer = endOdometer;
    }
}
