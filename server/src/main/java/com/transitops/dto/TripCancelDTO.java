package com.transitops.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for cancelling an active (DISPATCHED) trip.
 *
 * <p>A cancellation reason is mandatory — this field feeds into the trip audit
 * trail and is stored as {@code cancellation_reason} in the database.</p>
 */
public class TripCancelDTO {

    /** Reason for cancellation — stored for accountability and audit purposes */
    @NotBlank(message = "Cancellation reason is required")
    private String reason;


    public TripCancelDTO() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
