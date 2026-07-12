package com.transitops.service;

import com.transitops.domain.Expense;
import com.transitops.domain.FuelLog;
import com.transitops.domain.MaintenanceLog;
import com.transitops.domain.Trip;
import com.transitops.dto.VehicleFinancialSummaryDTO;
import com.transitops.repository.ExpenseRepository;
import com.transitops.repository.FuelLogRepository;
import com.transitops.repository.MaintenanceLogRepository;
import com.transitops.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service for calculating reporting data such as ROI and expenses.
 */
@Service
public class ReportingService {

    private final TripRepository tripRepository;
    private final FuelLogRepository fuelLogRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final ExpenseRepository expenseRepository;

    public ReportingService(TripRepository tripRepository,
                            FuelLogRepository fuelLogRepository,
                            MaintenanceLogRepository maintenanceLogRepository,
                            ExpenseRepository expenseRepository) {
        this.tripRepository = tripRepository;
        this.fuelLogRepository = fuelLogRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.expenseRepository = expenseRepository;
    }

    /**
     * Calculates the ROI (Return on Investment) for a specific vehicle.
     * Formula: (Total Revenue - (Fuel Cost + Maintenance Cost)) / (Fuel Cost + Maintenance Cost)
     *
     * @param vehicleId the vehicle ID
     * @return the calculated ROI
     */
    public BigDecimal calculateVehicleROI(Long vehicleId) {
        BigDecimal totalRevenue = totalRevenue(vehicleId);
        BigDecimal totalFuelCost = totalFuelCost(vehicleId);
        BigDecimal totalMaintenanceCost = totalMaintenanceCost(vehicleId);

        BigDecimal totalCost = totalFuelCost.add(totalMaintenanceCost);

        if (totalCost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO; // Avoid division by zero
        }

        return totalRevenue.subtract(totalCost)
                .divide(totalCost, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates total misc expenses (tolls, permits, etc.) for a specific vehicle.
     *
     * @param vehicleId the vehicle ID
     * @return total misc expense
     */
    public BigDecimal calculateMiscExpenseTotal(Long vehicleId) {
        return expenseRepository.findByVehicleId(vehicleId).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Returns a comprehensive financial summary for a vehicle.
     *
     * @param vehicleId the vehicle ID
     * @return the financial summary
     */
    public VehicleFinancialSummaryDTO getFinancialSummary(Long vehicleId) {
        return new VehicleFinancialSummaryDTO(
                calculateVehicleROI(vehicleId),
                totalFuelCost(vehicleId),
                totalMaintenanceCost(vehicleId),
                calculateMiscExpenseTotal(vehicleId)
        );
    }

    public BigDecimal totalFuelCost(Long vehicleId) {
        return fuelLogRepository.findByVehicleId(vehicleId).stream()
                .map(FuelLog::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalMaintenanceCost(Long vehicleId) {
        return maintenanceLogRepository.findByVehicleId(vehicleId).stream()
                .filter(log -> log.getFinalCost() != null)
                .map(MaintenanceLog::getFinalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalRevenue(Long vehicleId) {
        return tripRepository.findByVehicleId(vehicleId).stream()
                .filter(trip -> trip.getRevenue() != null && "COMPLETED".equals(trip.getStatus()))
                .map(Trip::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
