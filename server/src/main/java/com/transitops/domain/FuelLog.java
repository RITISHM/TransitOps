package com.transitops.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
