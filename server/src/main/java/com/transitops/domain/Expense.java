package com.transitops.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a Expense in the TransitOps system.
 */
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logged_by", nullable = false)
    private User loggedBy;

    @Column(name = "expense_type", nullable = false, length = 30)
    private String expenseType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "expense_date", nullable = false)
    private LocalDateTime expenseDate;

    @Column(name = "proof_image_url", length = 500)
    private String proofImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.expenseDate == null) {
            this.expenseDate = LocalDateTime.now();
        }
    }


    public Expense() {
    }

    public Expense(Long id, Trip trip, Vehicle vehicle, User loggedBy, String expenseType, BigDecimal amount, String description, LocalDateTime expenseDate, String proofImageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.trip = trip;
        this.vehicle = vehicle;
        this.loggedBy = loggedBy;
        this.expenseType = expenseType;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.proofImageUrl = proofImageUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public User getLoggedBy() {
        return loggedBy;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
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

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setLoggedBy(User loggedBy) {
        this.loggedBy = loggedBy;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpenseDate(LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setProofImageUrl(String proofImageUrl) {
        this.proofImageUrl = proofImageUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static ExpenseBuilder builder() {
        return new ExpenseBuilder();
    }

    public static class ExpenseBuilder {
        private Long id;
        private Trip trip;
        private Vehicle vehicle;
        private User loggedBy;
        private String expenseType;
        private BigDecimal amount;
        private String description;
        private LocalDateTime expenseDate;
        private String proofImageUrl;
        private LocalDateTime createdAt;
        public ExpenseBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public ExpenseBuilder trip(Trip trip) {
            this.trip = trip;
            return this;
        }
        public ExpenseBuilder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public ExpenseBuilder loggedBy(User loggedBy) {
            this.loggedBy = loggedBy;
            return this;
        }
        public ExpenseBuilder expenseType(String expenseType) {
            this.expenseType = expenseType;
            return this;
        }
        public ExpenseBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }
        public ExpenseBuilder description(String description) {
            this.description = description;
            return this;
        }
        public ExpenseBuilder expenseDate(LocalDateTime expenseDate) {
            this.expenseDate = expenseDate;
            return this;
        }
        public ExpenseBuilder proofImageUrl(String proofImageUrl) {
            this.proofImageUrl = proofImageUrl;
            return this;
        }
        public ExpenseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Expense build() {
            return new Expense(this.id, this.trip, this.vehicle, this.loggedBy, this.expenseType, this.amount, this.description, this.expenseDate, this.proofImageUrl, this.createdAt);
        }
    }
}
