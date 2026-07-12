package com.transitops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for creating a trip checkpoint (waypoint/stop).
 *
 * <p>Checkpoints are ordered stops along a trip route. They must be created
 * before the refuel-at-checkpoint flow can be used, since that flow requires
 * an existing checkpoint ID (gap #11 fix).</p>
 */
public class CheckpointCreateDTO {

    /** Name or description of this stop (e.g., "Fuel Station Highway 9") */
    @NotBlank(message = "Checkpoint name is required")
    private String name;

    /** Order of this checkpoint along the route (1-indexed) */
    @NotNull(message = "Checkpoint order is required")
    private Integer checkpointOrder;

    /** Optional estimated arrival time at this checkpoint */
    private LocalDateTime expectedArrival;


    public CheckpointCreateDTO() {
    }

    public String getName() { return name; }
    public Integer getCheckpointOrder() { return checkpointOrder; }
    public LocalDateTime getExpectedArrival() { return expectedArrival; }

    public void setName(String name) { this.name = name; }
    public void setCheckpointOrder(Integer checkpointOrder) { this.checkpointOrder = checkpointOrder; }
    public void setExpectedArrival(LocalDateTime expectedArrival) { this.expectedArrival = expectedArrival; }
}
