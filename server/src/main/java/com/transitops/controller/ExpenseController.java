package com.transitops.controller;

import com.transitops.domain.Expense;
import com.transitops.dto.ExpenseCreateDTO;
import com.transitops.security.AuthUser;
import com.transitops.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing generic expenses (tolls, permits, etc.).
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Creates a new generic expense.
     * Must be linked to a trip or a vehicle.
     */
    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Expense> create(
            @Valid @RequestBody ExpenseCreateDTO dto,
            @AuthenticationPrincipal AuthUser currentUser) {

        // Enforce DB constraint at API layer for a clean 400 response
        if (dto.getTripId() == null && dto.getVehicleId() == null) {
            throw new IllegalArgumentException("Expense must be linked to a trip or a vehicle.");
        }

        Expense expense = expenseService.create(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }
}
