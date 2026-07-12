package com.transitops.dto;

/**
 * Lightweight response DTO returned after a trip is successfully dispatched.
 *
 * <p>Contains only the essential confirmation fields a caller needs immediately
 * after dispatch — the full trip details can be fetched via GET /api/trips/{id}.</p>
 */
public class DispatchedTripResponseDTO {

    private Long tripId;
    private String status;
    private String vehicleRegistrationNumber;
    private String driverName;


    public DispatchedTripResponseDTO() {
    }

    public DispatchedTripResponseDTO(Long tripId, String status,
                                     String vehicleRegistrationNumber, String driverName) {
        this.tripId = tripId;
        this.status = status;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.driverName = driverName;
    }

    // ----- Getters -----

    public Long getTripId() { return tripId; }
    public String getStatus() { return status; }
    public String getVehicleRegistrationNumber() { return vehicleRegistrationNumber; }
    public String getDriverName() { return driverName; }

    // ----- Setters -----

    public void setTripId(Long tripId) { this.tripId = tripId; }
    public void setStatus(String status) { this.status = status; }
    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) { this.vehicleRegistrationNumber = vehicleRegistrationNumber; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
}
