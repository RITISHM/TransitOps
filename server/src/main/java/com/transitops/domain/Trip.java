package com.transitops.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
