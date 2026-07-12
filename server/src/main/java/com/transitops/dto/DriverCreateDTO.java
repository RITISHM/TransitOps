package com.transitops.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating a new Driver and its linked User account.
 *
 * <p>Because every Driver requires a 1:1 linked User row, this DTO bundles both
 * the user-level fields (name, email, password) and the driver-specific fields
 * (license details, contact info) into a single request payload. The service layer
 * creates both entities within a single {@code @Transactional} boundary to ensure
 * atomicity — a failure never leaves an orphaned User without a Driver or vice versa.</p>
 *
 * <p>The {@code licenseExpiryDate} is validated with {@code @Future} so that a
 * driver with an already-expired license is rejected at creation time with a clean
 * 400 error, rather than being silently accepted and only caught later at dispatch.</p>
 */
public class DriverCreateDTO {

    // ----- User fields -----

    /** Full name of the driver — stored on the linked User entity */
    @NotBlank(message = "Name is required")
    private String name;

    /** Email address — serves as the login username; must be unique system-wide */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** Initial password — will be BCrypt-hashed before storage */
    @NotBlank(message = "Password is required")
    private String password;

    // ----- Driver fields -----

    /** Government-issued driver license number — must be unique */
    @NotBlank(message = "License number is required")
    private String licenseNumber;

    /** License category (e.g., "LMV", "HMV", "HGMV") */
    private String licenseCategory;

    /** License expiry date — must be in the future at creation time */
    @NotNull(message = "License expiry date is required")
    @Future(message = "License expiry date must be in the future")
    private LocalDate licenseExpiryDate;

    /** Contact phone number */
    private String contactNumber;

    /** Date of birth — optional */
    private LocalDate dob;


    public DriverCreateDTO() {
    }

    // ----- Getters -----

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public LocalDate getDob() {
        return dob;
    }

    // ----- Setters -----

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
