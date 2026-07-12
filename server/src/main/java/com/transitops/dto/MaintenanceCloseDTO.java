package com.transitops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for closing a maintenance ticket.
 *
 * <p>Allows updating the vehicle's preventive maintenance schedule (gap #13 fix).</p>
 */
public class MaintenanceCloseDTO {

    /** Actual final cost of the maintenance */
    @NotNull(message = "Final cost is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Final cost cannot be negative")
    private BigDecimal finalCost;

    /** Optional: The next date the vehicle is due for service */
    private LocalDate nextServiceDueDate;

    /** Optional: The odometer reading when the next service is due */
    @DecimalMin(value = "0.00", inclusive = true, message = "Next service odometer cannot be negative")
    private BigDecimal nextServiceDueOdometer;

    public MaintenanceCloseDTO() {}

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }

    public LocalDate getNextServiceDueDate() {
        return nextServiceDueDate;
    }

    public void setNextServiceDueDate(LocalDate nextServiceDueDate) {
        this.nextServiceDueDate = nextServiceDueDate;
    }

    public BigDecimal getNextServiceDueOdometer() {
        return nextServiceDueOdometer;
    }

    public void setNextServiceDueOdometer(BigDecimal nextServiceDueOdometer) {
        this.nextServiceDueOdometer = nextServiceDueOdometer;
    }
}
