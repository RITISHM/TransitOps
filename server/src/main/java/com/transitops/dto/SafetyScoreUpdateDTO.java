package com.transitops.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for updating a driver's safety score.
 */
public class SafetyScoreUpdateDTO {

    @NotNull(message = "New score is required")
    @DecimalMin(value = "0.00", message = "Score cannot be less than 0.00")
    @DecimalMax(value = "100.00", message = "Score cannot exceed 100.00")
    private BigDecimal newScore;

    private String reason;

    public SafetyScoreUpdateDTO() {}

    public BigDecimal getNewScore() {
        return newScore;
    }

    public void setNewScore(BigDecimal newScore) {
        this.newScore = newScore;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
