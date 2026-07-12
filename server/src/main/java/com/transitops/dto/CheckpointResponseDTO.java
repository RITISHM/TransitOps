package com.transitops.dto;

import com.transitops.domain.TripCheckpoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning TripCheckpoint data in API responses.
 *
 * <p>Avoids exposing the full Trip entity reference — only includes the trip ID
 * for reference context.</p>
 */
public class CheckpointResponseDTO {

    private Long id;
    private Long tripId;
    private String name;
    private Integer checkpointOrder;
    private LocalDateTime expectedArrival;
    private LocalDateTime reachedAt;
    private BigDecimal odometerReadingOnArrival;
    private String status;
    private LocalDateTime createdAt;


    public CheckpointResponseDTO() {
    }

    /**
     * Factory method that converts a TripCheckpoint entity into a response DTO.
     *
     * @param checkpoint the JPA entity to convert
     * @return a fully populated response DTO
     */
    public static CheckpointResponseDTO fromEntity(TripCheckpoint checkpoint) {
        CheckpointResponseDTO dto = new CheckpointResponseDTO();
        dto.setId(checkpoint.getId());
        dto.setName(checkpoint.getName());
        dto.setCheckpointOrder(checkpoint.getCheckpointOrder());
        dto.setExpectedArrival(checkpoint.getExpectedArrival());
        dto.setReachedAt(checkpoint.getReachedAt());
        dto.setOdometerReadingOnArrival(checkpoint.getOdometerReadingOnArrival());
        dto.setStatus(checkpoint.getStatus());
        dto.setCreatedAt(checkpoint.getCreatedAt());

        if (checkpoint.getTrip() != null) {
            dto.setTripId(checkpoint.getTrip().getId());
        }

        return dto;
    }

    // ----- Getters -----

    public Long getId() { return id; }
    public Long getTripId() { return tripId; }
    public String getName() { return name; }
    public Integer getCheckpointOrder() { return checkpointOrder; }
    public LocalDateTime getExpectedArrival() { return expectedArrival; }
    public LocalDateTime getReachedAt() { return reachedAt; }
    public BigDecimal getOdometerReadingOnArrival() { return odometerReadingOnArrival; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ----- Setters -----

    public void setId(Long id) { this.id = id; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public void setName(String name) { this.name = name; }
    public void setCheckpointOrder(Integer checkpointOrder) { this.checkpointOrder = checkpointOrder; }
    public void setExpectedArrival(LocalDateTime expectedArrival) { this.expectedArrival = expectedArrival; }
    public void setReachedAt(LocalDateTime reachedAt) { this.reachedAt = reachedAt; }
    public void setOdometerReadingOnArrival(BigDecimal odometerReadingOnArrival) { this.odometerReadingOnArrival = odometerReadingOnArrival; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
