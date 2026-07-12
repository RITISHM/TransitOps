package com.transitops.dto;

import com.transitops.domain.Trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning Trip data in API responses.
 *
 * <p>Flattens the Vehicle and Driver relationships into their IDs and key display
 * fields (registration number, driver name) to avoid lazy-loading issues and
 * provide a self-contained JSON response.</p>
 */
public class TripResponseDTO {

    private Long id;
    private String source;
    private String destination;
    private BigDecimal expectedDistance;
    private BigDecimal cargoWeight;
    private BigDecimal revenue;
    private String status;
    private BigDecimal startOdometer;
    private BigDecimal endOdometer;
    private LocalDateTime dispatchedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Flattened Vehicle fields
    private Long vehicleId;
    private String vehicleRegistrationNumber;

    // Flattened Driver fields
    private Long driverId;
    private String driverName;


    public TripResponseDTO() {
    }

    /**
     * Factory method that converts a Trip entity into a response DTO.
     *
     * @param trip the JPA entity to convert — Vehicle and Driver must be loaded
     * @return a fully populated response DTO
     */
    public static TripResponseDTO fromEntity(Trip trip) {
        TripResponseDTO dto = new TripResponseDTO();
        dto.setId(trip.getId());
        dto.setSource(trip.getSource());
        dto.setDestination(trip.getDestination());
        dto.setExpectedDistance(trip.getExpectedDistance());
        dto.setCargoWeight(trip.getCargoWeight());
        dto.setRevenue(trip.getRevenue());
        dto.setStatus(trip.getStatus());
        dto.setStartOdometer(trip.getStartOdometer());
        dto.setEndOdometer(trip.getEndOdometer());
        dto.setDispatchedAt(trip.getDispatchedAt());
        dto.setCompletedAt(trip.getCompletedAt());
        dto.setCancelledAt(trip.getCancelledAt());
        dto.setCancellationReason(trip.getCancellationReason());
        dto.setCreatedAt(trip.getCreatedAt());
        dto.setUpdatedAt(trip.getUpdatedAt());

        if (trip.getVehicle() != null) {
            dto.setVehicleId(trip.getVehicle().getId());
            dto.setVehicleRegistrationNumber(trip.getVehicle().getRegistrationNumber());
        }
        if (trip.getDriver() != null) {
            dto.setDriverId(trip.getDriver().getId());
            if (trip.getDriver().getUser() != null) {
                dto.setDriverName(trip.getDriver().getUser().getName());
            }
        }

        return dto;
    }


    // ----- Getters -----

    public Long getId() { return id; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public BigDecimal getExpectedDistance() { return expectedDistance; }
    public BigDecimal getCargoWeight() { return cargoWeight; }
    public BigDecimal getRevenue() { return revenue; }
    public String getStatus() { return status; }
    public BigDecimal getStartOdometer() { return startOdometer; }
    public BigDecimal getEndOdometer() { return endOdometer; }
    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public String getCancellationReason() { return cancellationReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getVehicleId() { return vehicleId; }
    public String getVehicleRegistrationNumber() { return vehicleRegistrationNumber; }
    public Long getDriverId() { return driverId; }
    public String getDriverName() { return driverName; }

    // ----- Setters -----

    public void setId(Long id) { this.id = id; }
    public void setSource(String source) { this.source = source; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setExpectedDistance(BigDecimal expectedDistance) { this.expectedDistance = expectedDistance; }
    public void setCargoWeight(BigDecimal cargoWeight) { this.cargoWeight = cargoWeight; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
    public void setStatus(String status) { this.status = status; }
    public void setStartOdometer(BigDecimal startOdometer) { this.startOdometer = startOdometer; }
    public void setEndOdometer(BigDecimal endOdometer) { this.endOdometer = endOdometer; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) { this.vehicleRegistrationNumber = vehicleRegistrationNumber; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
}
