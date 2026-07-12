package com.transitops.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_logs")
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logged_by", nullable = false)
    private User loggedBy;

    @Column(name = "fuel_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal fuelQuantity;

    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "odometer_reading", nullable = false, precision = 10, scale = 2)
    private BigDecimal odometerReading;

    @Column(name = "refuel_date", nullable = false)
    private LocalDateTime refuelDate;

    @Column(name = "proof_image_url", length = 500)
    private String proofImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.refuelDate == null) {
            this.refuelDate = LocalDateTime.now();
        }
    }


    public FuelLog() {
    }

    public FuelLog(Long id, Vehicle vehicle, Trip trip, User loggedBy, BigDecimal fuelQuantity, BigDecimal totalCost, BigDecimal odometerReading, LocalDateTime refuelDate, String proofImageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.trip = trip;
        this.loggedBy = loggedBy;
        this.fuelQuantity = fuelQuantity;
        this.totalCost = totalCost;
        this.odometerReading = odometerReading;
        this.refuelDate = refuelDate;
        this.proofImageUrl = proofImageUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Trip getTrip() {
        return trip;
    }

    public User getLoggedBy() {
        return loggedBy;
    }

    public BigDecimal getFuelQuantity() {
        return fuelQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal getOdometerReading() {
        return odometerReading;
    }

    public LocalDateTime getRefuelDate() {
        return refuelDate;
    }

    public String getProofImageUrl() {
        return proofImageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setLoggedBy(User loggedBy) {
        this.loggedBy = loggedBy;
    }

    public void setFuelQuantity(BigDecimal fuelQuantity) {
        this.fuelQuantity = fuelQuantity;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setOdometerReading(BigDecimal odometerReading) {
        this.odometerReading = odometerReading;
    }

    public void setRefuelDate(LocalDateTime refuelDate) {
        this.refuelDate = refuelDate;
    }

    public void setProofImageUrl(String proofImageUrl) {
        this.proofImageUrl = proofImageUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static FuelLogBuilder builder() {
        return new FuelLogBuilder();
    }

    public static class FuelLogBuilder {
        private Long id;
        private Vehicle vehicle;
        private Trip trip;
        private User loggedBy;
        private BigDecimal fuelQuantity;
        private BigDecimal totalCost;
        private BigDecimal odometerReading;
        private LocalDateTime refuelDate;
        private String proofImageUrl;
        private LocalDateTime createdAt;
        public FuelLogBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public FuelLogBuilder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public FuelLogBuilder trip(Trip trip) {
            this.trip = trip;
            return this;
        }
        public FuelLogBuilder loggedBy(User loggedBy) {
            this.loggedBy = loggedBy;
            return this;
        }
        public FuelLogBuilder fuelQuantity(BigDecimal fuelQuantity) {
            this.fuelQuantity = fuelQuantity;
            return this;
        }
        public FuelLogBuilder totalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
            return this;
        }
        public FuelLogBuilder odometerReading(BigDecimal odometerReading) {
            this.odometerReading = odometerReading;
            return this;
        }
        public FuelLogBuilder refuelDate(LocalDateTime refuelDate) {
            this.refuelDate = refuelDate;
            return this;
        }
        public FuelLogBuilder proofImageUrl(String proofImageUrl) {
            this.proofImageUrl = proofImageUrl;
            return this;
        }
        public FuelLogBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public FuelLog build() {
            return new FuelLog(this.id, this.vehicle, this.trip, this.loggedBy, this.fuelQuantity, this.totalCost, this.odometerReading, this.refuelDate, this.proofImageUrl, this.createdAt);
        }
    }
}
