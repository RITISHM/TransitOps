package com.transitops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating a new generic Expense (e.g., tolls, permits).
 */
public class ExpenseCreateDTO {

    /** Optional: ID of the trip this expense is associated with */
    private Long tripId;

    /** Optional: ID of the vehicle this expense is associated with */
    private Long vehicleId;

    /** Type of expense (e.g., "TOLL", "PERMIT", "PARKING") */
    @NotBlank(message = "Expense type is required")
    private String expenseType;

    /** Monetary amount of the expense */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    /** Optional: Additional description */
    private String description;

    /** Optional: URL to an uploaded receipt image */
    private String proofImageUrl;

    public ExpenseCreateDTO() {}

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProofImageUrl() {
        return proofImageUrl;
    }

    public void setProofImageUrl(String proofImageUrl) {
        this.proofImageUrl = proofImageUrl;
    }
}
