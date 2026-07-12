package com.transitops.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain entity representing a Driver in the TransitOps system.
 */
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column
    private LocalDate dob;

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(name = "license_category", length = 20)
    private String licenseCategory;

    @Column(name = "license_expiry_date", nullable = false)
    private LocalDate licenseExpiryDate;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "safety_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal safetyScore = new BigDecimal("100.00");

    @Column(name = "safety_audit_notes", columnDefinition = "TEXT")
    private String safetyAuditNotes;

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


    public Driver() {
    }

    public Driver(Long id, User user, LocalDate dob, String licenseNumber, String licenseCategory, LocalDate licenseExpiryDate, String contactNumber, BigDecimal safetyScore, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.dob = dob;
        this.licenseNumber = licenseNumber;
        this.licenseCategory = licenseCategory;
        this.licenseExpiryDate = licenseExpiryDate;
        this.contactNumber = contactNumber;
        this.safetyScore = safetyScore;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getLicenseCategory() {
        return licenseCategory;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public BigDecimal getSafetyScore() {
        return safetyScore;
    }

    public String getSafetyAuditNotes() {
        return safetyAuditNotes;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setLicenseCategory(String licenseCategory) {
        this.licenseCategory = licenseCategory;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setSafetyScore(BigDecimal safetyScore) {
        this.safetyScore = safetyScore;
    }

    public void setSafetyAuditNotes(String safetyAuditNotes) {
        this.safetyAuditNotes = safetyAuditNotes;
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

    public static DriverBuilder builder() {
        return new DriverBuilder();
    }

    public static class DriverBuilder {
        private Long id;
        private User user;
        private LocalDate dob;
        private String licenseNumber;
        private String licenseCategory;
        private LocalDate licenseExpiryDate;
        private String contactNumber;
        private BigDecimal safetyScore;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        public DriverBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public DriverBuilder user(User user) {
            this.user = user;
            return this;
        }
        public DriverBuilder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }
        public DriverBuilder licenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }
        public DriverBuilder licenseCategory(String licenseCategory) {
            this.licenseCategory = licenseCategory;
            return this;
        }
        public DriverBuilder licenseExpiryDate(LocalDate licenseExpiryDate) {
            this.licenseExpiryDate = licenseExpiryDate;
            return this;
        }
        public DriverBuilder contactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }
        public DriverBuilder safetyScore(BigDecimal safetyScore) {
            this.safetyScore = safetyScore;
            return this;
        }
        public DriverBuilder status(String status) {
            this.status = status;
            return this;
        }
        public DriverBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public DriverBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public Driver build() {
            return new Driver(this.id, this.user, this.dob, this.licenseNumber, this.licenseCategory, this.licenseExpiryDate, this.contactNumber, this.safetyScore, this.status, this.createdAt, this.updatedAt);
        }
    }
}
