package com.transitops.dto;

import com.transitops.enums.VehicleType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for creating or updating a Vehicle.
 *
 * <p>Validation annotations ensure that mandatory fields are present and correctly
 * typed before reaching the service layer. The {@code vehicleType} field uses the
 * {@link VehicleType} enum to fix gap #16 (previously unconstrained free text).
 * The {@code regionId} is required to fix gap #18 (region was NOT NULL in DB but
 * not enforced at the API level).</p>
 */
public class VehicleCreateDTO {

    /** Vehicle registration plate number — must be unique system-wide */
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    /** Optional human-friendly name for the vehicle (e.g., "Big Blue Truck") */
    private String vehicleName;

    /** Constrained vehicle classification — see {@link VehicleType} enum */
    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    /** Fuel type descriptor (e.g., "DIESEL", "PETROL", "CNG", "ELECTRIC") */
    private String fuelType;

    /** Maximum cargo weight the vehicle can carry, in kilograms */
    @NotNull(message = "Max load capacity is required")
    @DecimalMin(value = "0.01", message = "Max load capacity must be greater than zero")
    private BigDecimal maxLoadCapacity;

    /** Purchase price of the vehicle — used in ROI calculations (Phase 8) */
    @NotNull(message = "Acquisition cost is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Acquisition cost cannot be negative")
    private BigDecimal acquisitionCost;

    /** Date the vehicle was acquired — optional but useful for depreciation tracking */
    private LocalDate acquisitionDate;

    /** ID of the region this vehicle is assigned to — required for dashboard filtering */
    @NotNull(message = "Region ID is required")
    private Long regionId;


    public VehicleCreateDTO() {
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getMaxLoadCapacity() {
        return maxLoadCapacity;
    }

    public void setMaxLoadCapacity(BigDecimal maxLoadCapacity) {
        this.maxLoadCapacity = maxLoadCapacity;
    }

    public BigDecimal getAcquisitionCost() {
        return acquisitionCost;
    }

    public void setAcquisitionCost(BigDecimal acquisitionCost) {
        this.acquisitionCost = acquisitionCost;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
}
