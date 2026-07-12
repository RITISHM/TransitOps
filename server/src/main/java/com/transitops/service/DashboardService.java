package com.transitops.service;

import com.transitops.dto.DashboardKpiDTO;
import com.transitops.enums.DriverStatus;
import com.transitops.enums.TripStatus;
import com.transitops.enums.VehicleStatus;
import com.transitops.repository.DriverRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * Service for calculating dashboard Key Performance Indicators.
 */
@Service
public class DashboardService {

    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;

    public DashboardService(VehicleRepository vehicleRepository,
                            TripRepository tripRepository,
                            DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
    }

    /**
     * Calculates the KPIs for the dashboard.
     * KPI DEFINITIONS (resolves gap #19 — terms used loosely in PDF §3.2):
     *  - Active Vehicles      = count(vehicles WHERE status != 'RETIRED')
     *  - Available Vehicles   = count(vehicles WHERE status = 'AVAILABLE')
     *  - Vehicles in Maintenance = count(vehicles WHERE status = 'IN_SHOP')
     *  - Active Trips         = count(trips WHERE status = 'DISPATCHED')
     *  - Pending Trips        = count(trips WHERE status = 'DRAFT')
     *  - Drivers On Duty      = count(drivers WHERE status IN ('AVAILABLE','ON_TRIP'))
     *  - Fleet Utilization %  = count(vehicles WHERE status='ON_TRIP') / count(vehicles WHERE status != 'RETIRED') * 100
     */
    public DashboardKpiDTO getDashboardKpis() {
        long activeVehicles = vehicleRepository.countByStatusNot(VehicleStatus.RETIRED.name());
        long availableVehicles = vehicleRepository.countByStatus(VehicleStatus.AVAILABLE.name());
        long vehiclesInMaintenance = vehicleRepository.countByStatus(VehicleStatus.IN_SHOP.name());
        
        long activeTrips = tripRepository.countByStatus(TripStatus.DISPATCHED.name());
        long pendingTrips = tripRepository.countByStatus(TripStatus.DRAFT.name());
        
        long driversOnDuty = driverRepository.countByStatusIn(Arrays.asList(
                DriverStatus.AVAILABLE.name(), DriverStatus.ON_TRIP.name()));
        
        long vehiclesOnTrip = vehicleRepository.countByStatus(VehicleStatus.ON_TRIP.name());
        
        BigDecimal fleetUtilizationPercentage = BigDecimal.ZERO;
        if (activeVehicles > 0) {
            fleetUtilizationPercentage = BigDecimal.valueOf(vehiclesOnTrip)
                    .divide(BigDecimal.valueOf(activeVehicles), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return new DashboardKpiDTO(
                activeVehicles,
                availableVehicles,
                vehiclesInMaintenance,
                activeTrips,
                pendingTrips,
                driversOnDuty,
                fleetUtilizationPercentage
        );
    }
}
