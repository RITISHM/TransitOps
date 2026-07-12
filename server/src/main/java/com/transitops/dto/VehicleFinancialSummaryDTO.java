package com.transitops.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for vehicle financial reporting.
 */
public class VehicleFinancialSummaryDTO {

    private BigDecimal roi;
    private BigDecimal totalFuelCost;
    private BigDecimal totalMaintenanceCost;
    private BigDecimal miscExpenseTotal;

    public VehicleFinancialSummaryDTO() {}

    public VehicleFinancialSummaryDTO(BigDecimal roi, BigDecimal totalFuelCost,
                                      BigDecimal totalMaintenanceCost, BigDecimal miscExpenseTotal) {
        this.roi = roi;
        this.totalFuelCost = totalFuelCost;
        this.totalMaintenanceCost = totalMaintenanceCost;
        this.miscExpenseTotal = miscExpenseTotal;
    }

    public BigDecimal getRoi() {
        return roi;
    }

    public void setRoi(BigDecimal roi) {
        this.roi = roi;
    }

    public BigDecimal getTotalFuelCost() {
        return totalFuelCost;
    }

    public void setTotalFuelCost(BigDecimal totalFuelCost) {
        this.totalFuelCost = totalFuelCost;
    }

    public BigDecimal getTotalMaintenanceCost() {
        return totalMaintenanceCost;
    }

    public void setTotalMaintenanceCost(BigDecimal totalMaintenanceCost) {
        this.totalMaintenanceCost = totalMaintenanceCost;
    }

    public BigDecimal getMiscExpenseTotal() {
        return miscExpenseTotal;
    }

    public void setMiscExpenseTotal(BigDecimal miscExpenseTotal) {
        this.miscExpenseTotal = miscExpenseTotal;
    }
}
