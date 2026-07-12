package com.transitops.dto;

import com.transitops.domain.Driver;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning Driver data in API responses.
 *
 * <p>Flattens the linked User entity into inline fields (userId, name, email, role)
 * to avoid lazy-loading pitfalls and provide a self-contained JSON response.</p>
 */
public class DriverResponseDTO {

    // ----- Driver fields -----
    private Long id;
    private LocalDate dob;
    private String licenseNumber;
    private String licenseCategory;
    private LocalDate licenseExpiryDate;
    private String contactNumber;
    private BigDecimal safetyScore;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ----- Flattened User fields -----
    private Long userId;
    private String name;
    private String email;
    private String role;


    public DriverResponseDTO() {
    }

    /**
     * Factory method that converts a Driver entity (with its linked User) into
     * a response DTO.
     *
     * @param driver the JPA entity to convert — must have its User relationship loaded
     * @return a fully populated response DTO
     */
    public static DriverResponseDTO fromEntity(Driver driver) {
        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setId(driver.getId());
        dto.setDob(driver.getDob());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setLicenseCategory(driver.getLicenseCategory());
        dto.setLicenseExpiryDate(driver.getLicenseExpiryDate());
        dto.setContactNumber(driver.getContactNumber());
        dto.setSafetyScore(driver.getSafetyScore());
        dto.setStatus(driver.getStatus());
        dto.setCreatedAt(driver.getCreatedAt());
        dto.setUpdatedAt(driver.getUpdatedAt());

        // Flatten the linked User entity
        if (driver.getUser() != null) {
            dto.setUserId(driver.getUser().getId());
            dto.setName(driver.getUser().getName());
            dto.setEmail(driver.getUser().getEmail());
            dto.setRole(driver.getUser().getRole());
        }

        return dto;
    }


    // ----- Getters -----

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // ----- Setters -----

    public void setId(Long id) {
        this.id = id;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
