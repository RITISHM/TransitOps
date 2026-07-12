package com.transitops.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_logs")
public class MaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logged_by", nullable = false)
    private User loggedBy;

    @Column(name = "maintenance_type", length = 50)
    private String maintenanceType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "expected_cost", precision = 12, scale = 2)
    private BigDecimal expectedCost;

    @Column(name = "final_cost", precision = 12, scale = 2)
    private BigDecimal finalCost;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

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


    public MaintenanceLog() {
    }

    public MaintenanceLog(Long id, Vehicle vehicle, User loggedBy, String maintenanceType, String description, BigDecimal expectedCost, BigDecimal finalCost, LocalDate startDate, LocalDate endDate, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.loggedBy = loggedBy;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.expectedCost = expectedCost;
        this.finalCost = finalCost;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public User getLoggedBy() {
        return loggedBy;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getExpectedCost() {
        return expectedCost;
    }

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
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

    public void setLoggedBy(User loggedBy) {
        this.loggedBy = loggedBy;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpectedCost(BigDecimal expectedCost) {
        this.expectedCost = expectedCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public static MaintenanceLogBuilder builder() {
        return new MaintenanceLogBuilder();
    }

    public static class MaintenanceLogBuilder {
        private Long id;
        private Vehicle vehicle;
        private User loggedBy;
        private String maintenanceType;
        private String description;
        private BigDecimal expectedCost;
        private BigDecimal finalCost;
        private LocalDate startDate;
        private LocalDate endDate;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        public MaintenanceLogBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public MaintenanceLogBuilder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public MaintenanceLogBuilder loggedBy(User loggedBy) {
            this.loggedBy = loggedBy;
            return this;
        }
        public MaintenanceLogBuilder maintenanceType(String maintenanceType) {
            this.maintenanceType = maintenanceType;
            return this;
        }
        public MaintenanceLogBuilder description(String description) {
            this.description = description;
            return this;
        }
        public MaintenanceLogBuilder expectedCost(BigDecimal expectedCost) {
            this.expectedCost = expectedCost;
            return this;
        }
        public MaintenanceLogBuilder finalCost(BigDecimal finalCost) {
            this.finalCost = finalCost;
            return this;
        }
        public MaintenanceLogBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }
        public MaintenanceLogBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public MaintenanceLogBuilder status(String status) {
            this.status = status;
            return this;
        }
        public MaintenanceLogBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public MaintenanceLogBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public MaintenanceLog build() {
            return new MaintenanceLog(this.id, this.vehicle, this.loggedBy, this.maintenanceType, this.description, this.expectedCost, this.finalCost, this.startDate, this.endDate, this.status, this.createdAt, this.updatedAt);
        }
    }
}
