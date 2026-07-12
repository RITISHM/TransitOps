package com.transitops.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object containing all Key Performance Indicators for the dashboard.
 */
public class DashboardKpiDTO {

    private long activeVehicles;
    private long availableVehicles;
    private long vehiclesInMaintenance;
    private long activeTrips;
    private long pendingTrips;
    private long driversOnDuty;
    private BigDecimal fleetUtilizationPercentage;

    public DashboardKpiDTO() {}

    public DashboardKpiDTO(long activeVehicles, long availableVehicles, long vehiclesInMaintenance,
                           long activeTrips, long pendingTrips, long driversOnDuty,
                           BigDecimal fleetUtilizationPercentage) {
        this.activeVehicles = activeVehicles;
        this.availableVehicles = availableVehicles;
        this.vehiclesInMaintenance = vehiclesInMaintenance;
        this.activeTrips = activeTrips;
        this.pendingTrips = pendingTrips;
        this.driversOnDuty = driversOnDuty;
        this.fleetUtilizationPercentage = fleetUtilizationPercentage;
    }

    public long getActiveVehicles() { return activeVehicles; }
    public void setActiveVehicles(long activeVehicles) { this.activeVehicles = activeVehicles; }

    public long getAvailableVehicles() { return availableVehicles; }
    public void setAvailableVehicles(long availableVehicles) { this.availableVehicles = availableVehicles; }

    public long getVehiclesInMaintenance() { return vehiclesInMaintenance; }
    public void setVehiclesInMaintenance(long vehiclesInMaintenance) { this.vehiclesInMaintenance = vehiclesInMaintenance; }

    public long getActiveTrips() { return activeTrips; }
    public void setActiveTrips(long activeTrips) { this.activeTrips = activeTrips; }

    public long getPendingTrips() { return pendingTrips; }
    public void setPendingTrips(long pendingTrips) { this.pendingTrips = pendingTrips; }

    public long getDriversOnDuty() { return driversOnDuty; }
    public void setDriversOnDuty(long driversOnDuty) { this.driversOnDuty = driversOnDuty; }

    public BigDecimal getFleetUtilizationPercentage() { return fleetUtilizationPercentage; }
    public void setFleetUtilizationPercentage(BigDecimal fleetUtilizationPercentage) { this.fleetUtilizationPercentage = fleetUtilizationPercentage; }
}
