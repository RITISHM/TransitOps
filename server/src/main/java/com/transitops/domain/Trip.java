package com.transitops.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a Trip in the TransitOps system.
 */
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(nullable = false, length = 255)
    private String source;

    @Column(nullable = false, length = 255)
    private String destination;

    @Column(name = "expected_distance", precision = 10, scale = 2)
    private BigDecimal expectedDistance;

    @Column(name = "cargo_weight", precision = 10, scale = 2)
    private BigDecimal cargoWeight;

    @Column(precision = 12, scale = 2)
    private BigDecimal revenue;

    @Column(name = "dispatched_at")
    private LocalDateTime dispatchedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", length = 255)
    private String cancellationReason;

    @Column(name = "start_odometer", precision = 10, scale = 2)
    private BigDecimal startOdometer;

    @Column(name = "end_odometer", precision = 10, scale = 2)
    private BigDecimal endOdometer;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public Trip() {
    }

    public Trip(Long id, Vehicle vehicle, Driver driver, String source, String destination, BigDecimal expectedDistance, BigDecimal cargoWeight, BigDecimal revenue, LocalDateTime dispatchedAt, LocalDateTime completedAt, LocalDateTime cancelledAt, String cancellationReason, BigDecimal startOdometer, BigDecimal endOdometer, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.expectedDistance = expectedDistance;
        this.cargoWeight = cargoWeight;
        this.revenue = revenue;
        this.dispatchedAt = dispatchedAt;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
        this.cancellationReason = cancellationReason;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public BigDecimal getExpectedDistance() {
        return expectedDistance;
    }

    public BigDecimal getCargoWeight() {
        return cargoWeight;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public LocalDateTime getDispatchedAt() {
        return dispatchedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public BigDecimal getStartOdometer() {
        return startOdometer;
    }

    public BigDecimal getEndOdometer() {
        return endOdometer;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setExpectedDistance(BigDecimal expectedDistance) {
        this.expectedDistance = expectedDistance;
    }

    public void setCargoWeight(BigDecimal cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public void setDispatchedAt(LocalDateTime dispatchedAt) {
        this.dispatchedAt = dispatchedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public void setStartOdometer(BigDecimal startOdometer) {
        this.startOdometer = startOdometer;
    }

    public void setEndOdometer(BigDecimal endOdometer) {
        this.endOdometer = endOdometer;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static TripBuilder builder() {
        return new TripBuilder();
    }

    public static class TripBuilder {
        private Long id;
        private Vehicle vehicle;
        private Driver driver;
        private String source;
        private String destination;
        private BigDecimal expectedDistance;
        private BigDecimal cargoWeight;
        private BigDecimal revenue;
        private LocalDateTime dispatchedAt;
        private LocalDateTime completedAt;
        private LocalDateTime cancelledAt;
        private String cancellationReason;
        private BigDecimal startOdometer;
        private BigDecimal endOdometer;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        public TripBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public TripBuilder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public TripBuilder driver(Driver driver) {
            this.driver = driver;
            return this;
        }
        public TripBuilder source(String source) {
            this.source = source;
            return this;
        }
        public TripBuilder destination(String destination) {
            this.destination = destination;
            return this;
        }
        public TripBuilder expectedDistance(BigDecimal expectedDistance) {
            this.expectedDistance = expectedDistance;
            return this;
        }
        public TripBuilder cargoWeight(BigDecimal cargoWeight) {
            this.cargoWeight = cargoWeight;
            return this;
        }
        public TripBuilder revenue(BigDecimal revenue) {
            this.revenue = revenue;
            return this;
        }
        public TripBuilder dispatchedAt(LocalDateTime dispatchedAt) {
            this.dispatchedAt = dispatchedAt;
            return this;
        }
        public TripBuilder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }
        public TripBuilder cancelledAt(LocalDateTime cancelledAt) {
            this.cancelledAt = cancelledAt;
            return this;
        }
        public TripBuilder cancellationReason(String cancellationReason) {
            this.cancellationReason = cancellationReason;
            return this;
        }
        public TripBuilder startOdometer(BigDecimal startOdometer) {
            this.startOdometer = startOdometer;
            return this;
        }
        public TripBuilder endOdometer(BigDecimal endOdometer) {
            this.endOdometer = endOdometer;
            return this;
        }
        public TripBuilder status(String status) {
            this.status = status;
            return this;
        }
        public TripBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public TripBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public Trip build() {
            return new Trip(this.id, this.vehicle, this.driver, this.source, this.destination, this.expectedDistance, this.cargoWeight, this.revenue, this.dispatchedAt, this.completedAt, this.cancelledAt, this.cancellationReason, this.startOdometer, this.endOdometer, this.status, this.createdAt, this.updatedAt);
        }
    }
}
