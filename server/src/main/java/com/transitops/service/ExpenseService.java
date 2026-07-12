package com.transitops.service;

import com.transitops.domain.Expense;
import com.transitops.domain.Trip;
import com.transitops.domain.Vehicle;
import com.transitops.dto.ExpenseCreateDTO;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.ExpenseRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.security.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for managing generic expenses (tolls, permits, etc.).
 */
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          TripRepository tripRepository,
                          VehicleRepository vehicleRepository) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Creates a new generic expense linked to a vehicle and/or trip.
     */
    @Transactional
    public Expense create(ExpenseCreateDTO dto, AuthUser currentUser) {
        Expense expense = new Expense();
        expense.setExpenseType(dto.getExpenseType());
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setProofImageUrl(dto.getProofImageUrl());
        expense.setExpenseDate(LocalDateTime.now());
        expense.setLoggedBy(currentUser.getUser());

        if (dto.getTripId() != null) {
            Trip trip = tripRepository.findById(dto.getTripId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
            expense.setTrip(trip);
        }

        if (dto.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
            expense.setVehicle(vehicle);
        }

        return expenseRepository.save(expense);
    }
}
