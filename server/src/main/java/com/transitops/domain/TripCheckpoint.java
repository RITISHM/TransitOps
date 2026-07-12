package com.transitops.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a TripCheckpoint in the TransitOps system.
 */
@Entity
@Table(name = "trip_checkpoints")
public class TripCheckpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "checkpoint_order", nullable = false)
    private Integer checkpointOrder;

    @Column(name = "expected_arrival")
    private LocalDateTime expectedArrival;

    @Column(name = "reached_at")
    private LocalDateTime reachedAt;

    @Column(name = "odometer_reading_on_arrival", precision = 10, scale = 2)
    private BigDecimal odometerReadingOnArrival;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    public TripCheckpoint() {
    }

    public TripCheckpoint(Long id, Trip trip, String name, Integer checkpointOrder, LocalDateTime expectedArrival, LocalDateTime reachedAt, BigDecimal odometerReadingOnArrival, String status, LocalDateTime createdAt) {
        this.id = id;
        this.trip = trip;
        this.name = name;
        this.checkpointOrder = checkpointOrder;
        this.expectedArrival = expectedArrival;
        this.reachedAt = reachedAt;
        this.odometerReadingOnArrival = odometerReadingOnArrival;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public String getName() {
        return name;
    }

    public Integer getCheckpointOrder() {
        return checkpointOrder;
    }

    public LocalDateTime getExpectedArrival() {
        return expectedArrival;
    }

    public LocalDateTime getReachedAt() {
        return reachedAt;
    }

    public BigDecimal getOdometerReadingOnArrival() {
        return odometerReadingOnArrival;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckpointOrder(Integer checkpointOrder) {
        this.checkpointOrder = checkpointOrder;
    }

    public void setExpectedArrival(LocalDateTime expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    public void setReachedAt(LocalDateTime reachedAt) {
        this.reachedAt = reachedAt;
    }

    public void setOdometerReadingOnArrival(BigDecimal odometerReadingOnArrival) {
        this.odometerReadingOnArrival = odometerReadingOnArrival;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static TripCheckpointBuilder builder() {
        return new TripCheckpointBuilder();
    }

    public static class TripCheckpointBuilder {
        private Long id;
        private Trip trip;
        private String name;
        private Integer checkpointOrder;
        private LocalDateTime expectedArrival;
        private LocalDateTime reachedAt;
        private BigDecimal odometerReadingOnArrival;
        private String status;
        private LocalDateTime createdAt;
        public TripCheckpointBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public TripCheckpointBuilder trip(Trip trip) {
            this.trip = trip;
            return this;
        }
        public TripCheckpointBuilder name(String name) {
            this.name = name;
            return this;
        }
        public TripCheckpointBuilder checkpointOrder(Integer checkpointOrder) {
            this.checkpointOrder = checkpointOrder;
            return this;
        }
        public TripCheckpointBuilder expectedArrival(LocalDateTime expectedArrival) {
            this.expectedArrival = expectedArrival;
            return this;
        }
        public TripCheckpointBuilder reachedAt(LocalDateTime reachedAt) {
            this.reachedAt = reachedAt;
            return this;
        }
        public TripCheckpointBuilder odometerReadingOnArrival(BigDecimal odometerReadingOnArrival) {
            this.odometerReadingOnArrival = odometerReadingOnArrival;
            return this;
        }
        public TripCheckpointBuilder status(String status) {
            this.status = status;
            return this;
        }
        public TripCheckpointBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public TripCheckpoint build() {
            return new TripCheckpoint(this.id, this.trip, this.name, this.checkpointOrder, this.expectedArrival, this.reachedAt, this.odometerReadingOnArrival, this.status, this.createdAt);
        }
    }
}
