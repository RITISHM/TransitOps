package com.transitops.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain entity representing a Vehicle in the TransitOps system.
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true, length = 30)
    private String registrationNumber;

    @Column(name = "vehicle_name", length = 100)
    private String vehicleName;

    @Column(name = "vehicle_type", nullable = false, length = 20)
    private String vehicleType;

    @Column(name = "fuel_type", length = 20)
    private String fuelType;

    @Column(name = "max_load_capacity", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxLoadCapacity;

    @Column(name = "current_odometer", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentOdometer = BigDecimal.ZERO;

    @Column(name = "acquisition_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal acquisitionCost;

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "next_maintenance_due_date")
    private LocalDate nextMaintenanceDueDate;

    @Column(name = "next_maintenance_due_odometer", precision = 10, scale = 2)
    private BigDecimal nextMaintenanceDueOdometer;

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


    public Vehicle() {
    }

    public Vehicle(Long id, String registrationNumber, String vehicleName, String vehicleType, String fuelType, BigDecimal maxLoadCapacity, BigDecimal currentOdometer, BigDecimal acquisitionCost, LocalDate acquisitionDate, Region region, String status, LocalDate nextMaintenanceDueDate, BigDecimal nextMaintenanceDueOdometer, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.maxLoadCapacity = maxLoadCapacity;
        this.currentOdometer = currentOdometer;
        this.acquisitionCost = acquisitionCost;
        this.acquisitionDate = acquisitionDate;
        this.region = region;
        this.status = status;
        this.nextMaintenanceDueDate = nextMaintenanceDueDate;
        this.nextMaintenanceDueOdometer = nextMaintenanceDueOdometer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public BigDecimal getMaxLoadCapacity() {
        return maxLoadCapacity;
    }

    public BigDecimal getCurrentOdometer() {
        return currentOdometer;
    }

    public BigDecimal getAcquisitionCost() {
        return acquisitionCost;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public Region getRegion() {
        return region;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getNextMaintenanceDueDate() {
        return nextMaintenanceDueDate;
    }

    public BigDecimal getNextMaintenanceDueOdometer() {
        return nextMaintenanceDueOdometer;
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

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setMaxLoadCapacity(BigDecimal maxLoadCapacity) {
        this.maxLoadCapacity = maxLoadCapacity;
    }

    public void setCurrentOdometer(BigDecimal currentOdometer) {
        this.currentOdometer = currentOdometer;
    }

    public void setAcquisitionCost(BigDecimal acquisitionCost) {
        this.acquisitionCost = acquisitionCost;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNextMaintenanceDueDate(LocalDate nextMaintenanceDueDate) {
        this.nextMaintenanceDueDate = nextMaintenanceDueDate;
    }

    public void setNextMaintenanceDueOdometer(BigDecimal nextMaintenanceDueOdometer) {
        this.nextMaintenanceDueOdometer = nextMaintenanceDueOdometer;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static VehicleBuilder builder() {
        return new VehicleBuilder();
    }

    public static class VehicleBuilder {
        private Long id;
        private String registrationNumber;
        private String vehicleName;
        private String vehicleType;
        private String fuelType;
        private BigDecimal maxLoadCapacity;
        private BigDecimal currentOdometer;
        private BigDecimal acquisitionCost;
        private LocalDate acquisitionDate;
        private Region region;
        private String status;
        private LocalDate nextMaintenanceDueDate;
        private BigDecimal nextMaintenanceDueOdometer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        public VehicleBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public VehicleBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }
        public VehicleBuilder vehicleName(String vehicleName) {
            this.vehicleName = vehicleName;
            return this;
        }
        public VehicleBuilder vehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }
        public VehicleBuilder fuelType(String fuelType) {
            this.fuelType = fuelType;
            return this;
        }
        public VehicleBuilder maxLoadCapacity(BigDecimal maxLoadCapacity) {
            this.maxLoadCapacity = maxLoadCapacity;
            return this;
        }
        public VehicleBuilder currentOdometer(BigDecimal currentOdometer) {
            this.currentOdometer = currentOdometer;
            return this;
        }
        public VehicleBuilder acquisitionCost(BigDecimal acquisitionCost) {
            this.acquisitionCost = acquisitionCost;
            return this;
        }
        public VehicleBuilder acquisitionDate(LocalDate acquisitionDate) {
            this.acquisitionDate = acquisitionDate;
            return this;
        }
        public VehicleBuilder region(Region region) {
            this.region = region;
            return this;
        }
        public VehicleBuilder status(String status) {
            this.status = status;
            return this;
        }
        public VehicleBuilder nextMaintenanceDueDate(LocalDate nextMaintenanceDueDate) {
            this.nextMaintenanceDueDate = nextMaintenanceDueDate;
            return this;
        }
        public VehicleBuilder nextMaintenanceDueOdometer(BigDecimal nextMaintenanceDueOdometer) {
            this.nextMaintenanceDueOdometer = nextMaintenanceDueOdometer;
            return this;
        }
        public VehicleBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public VehicleBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public Vehicle build() {
            return new Vehicle(this.id, this.registrationNumber, this.vehicleName, this.vehicleType, this.fuelType, this.maxLoadCapacity, this.currentOdometer, this.acquisitionCost, this.acquisitionDate, this.region, this.status, this.nextMaintenanceDueDate, this.nextMaintenanceDueOdometer, this.createdAt, this.updatedAt);
        }
    }
}
