package com.transitops.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_checkpoints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private String status = "PENDING";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
