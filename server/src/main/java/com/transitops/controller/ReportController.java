package com.transitops.controller;

import com.transitops.domain.Vehicle;
import com.transitops.dto.VehicleFinancialSummaryDTO;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.ReportingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.util.List;

/**
 * Controller for reporting and CSV exports.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportingService reportingService;
    private final VehicleRepository vehicleRepository;

    public ReportController(ReportingService reportingService, VehicleRepository vehicleRepository) {
        this.reportingService = reportingService;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Retrieves the financial summary for a specific vehicle.
     */
    @GetMapping("/vehicles/{id}/financial-summary")
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'FINANCIAL_ANALYST')")
    public VehicleFinancialSummaryDTO getFinancialSummary(@PathVariable Long id) {
        return reportingService.getFinancialSummary(id);
    }

    /**
     * Exports a CSV of vehicle financials.
     */
    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'FINANCIAL_ANALYST')")
    public ResponseEntity<String> exportCsv(@RequestParam String type, @RequestParam String format) {
        if (!"vehicle-financials".equals(type) || !"csv".equals(format)) {
            throw new IllegalArgumentException("Unsupported export type or format");
        }

        List<Vehicle> vehicles = vehicleRepository.findAll();
        StringWriter sw = new StringWriter();
        
        // CSV Header
        sw.append("Vehicle ID,Registration Number,ROI,Total Fuel Cost,Total Maintenance Cost,Misc Expense Total\n");

        for (Vehicle v : vehicles) {
            VehicleFinancialSummaryDTO summary = reportingService.getFinancialSummary(v.getId());
            
            sw.append(String.valueOf(v.getId())).append(",");
            sw.append(escapeCsv(v.getRegistrationNumber())).append(",");
            sw.append(String.valueOf(summary.getRoi())).append(",");
            sw.append(String.valueOf(summary.getTotalFuelCost())).append(",");
            sw.append(String.valueOf(summary.getTotalMaintenanceCost())).append(",");
            sw.append(String.valueOf(summary.getMiscExpenseTotal())).append("\n");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"vehicle-financials.csv\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(sw.toString());
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
