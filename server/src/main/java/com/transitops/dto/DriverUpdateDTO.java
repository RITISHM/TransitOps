package com.transitops.dto;

import jakarta.validation.constraints.Future;

import java.time.LocalDate;

/**
 * Data Transfer Object for updating an existing Driver's mutable fields.
 *
 * <p>Unlike {@link DriverCreateDTO}, this DTO does not include User-level fields
 * (name, email, password) because those are identity credentials that should be
 * managed through a separate user-administration flow. This DTO only exposes the
 * driver-specific operational fields that a Fleet Manager would routinely update.</p>
 *
 * <p>All fields are optional — only non-null fields will be applied as updates.
 * If {@code licenseExpiryDate} is provided, it must be in the future.</p>
 */
public class DriverUpdateDTO {

    /** Updated license number */
    private String licenseNumber;

    /** Updated license category */
    private String licenseCategory;

    /** Updated license expiry — must be in the future if provided */
    @Future(message = "License expiry date must be in the future")
    private LocalDate licenseExpiryDate;

    /** Updated contact phone number */
    private String contactNumber;

    /** Updated date of birth */
    private LocalDate dob;

    /** Updated driver status (e.g., AVAILABLE, OFF_DUTY, SUSPENDED) */
    private String status;


    public DriverUpdateDTO() {
    }

    // ----- Getters -----

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

    public LocalDate getDob() {
        return dob;
    }

    public String getStatus() {
        return status;
    }

    // ----- Setters -----

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

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
