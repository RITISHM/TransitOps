package com.transitops.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
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
}
