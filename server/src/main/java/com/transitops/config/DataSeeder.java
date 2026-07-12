package com.transitops.config;

import com.transitops.domain.*;
import com.transitops.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, RegionRepository regionRepository, 
                      VehicleRepository vehicleRepository, DriverRepository driverRepository, 
                      TripRepository tripRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.tripRepository = tripRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            System.out.println("Data already seeded. Skipping...");
            return;
        }

        System.out.println("Seeding mock data for local testing...");

        // 1. Create Users
        User adminUser = new User();
        adminUser.setName("Admin Manager");
        adminUser.setEmail("admin@transitops.com");
        adminUser.setPasswordHash(passwordEncoder.encode("password123"));
        adminUser.setRole("FLEET_MANAGER");
        adminUser.setIsActive(true);
        userRepository.save(adminUser);

        User driverUser1 = new User();
        driverUser1.setName("Alex Driver");
        driverUser1.setEmail("alex@transitops.com");
        driverUser1.setPasswordHash(passwordEncoder.encode("password123"));
        driverUser1.setRole("DRIVER");
        driverUser1.setIsActive(true);
        userRepository.save(driverUser1);
        
        User driverUser2 = new User();
        driverUser2.setName("John Route");
        driverUser2.setEmail("john@transitops.com");
        driverUser2.setPasswordHash(passwordEncoder.encode("password123"));
        driverUser2.setRole("DRIVER");
        driverUser2.setIsActive(true);
        userRepository.save(driverUser2);

        // 2. Create Regions
        Region regionNorth = new Region();
        regionNorth.setName("North Zone");
        regionNorth.setCode("NZ-01");
        regionRepository.save(regionNorth);

        // 3. Create Vehicles
        Vehicle v1 = new Vehicle();
        v1.setRegistrationNumber("VAN-05");
        v1.setVehicleName("Ford Transit");
        v1.setVehicleType("VAN");
        v1.setMaxLoadCapacity(new BigDecimal("1500.00"));
        v1.setFuelType("DIESEL");
        v1.setAcquisitionDate(LocalDate.of(2022, 1, 15));
        v1.setAcquisitionCost(new BigDecimal("35000.00"));
        v1.setCurrentOdometer(new BigDecimal("12500.50"));
        v1.setStatus("ACTIVE");
        v1.setRegion(regionNorth);
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle();
        v2.setRegistrationNumber("TRK-12");
        v2.setVehicleName("Volvo FH");
        v2.setVehicleType("TRUCK");
        v2.setMaxLoadCapacity(new BigDecimal("25000.00"));
        v2.setFuelType("DIESEL");
        v2.setAcquisitionDate(LocalDate.of(2021, 5, 20));
        v2.setAcquisitionCost(new BigDecimal("85000.00"));
        v2.setCurrentOdometer(new BigDecimal("45200.75"));
        v2.setStatus("MAINTENANCE");
        v2.setRegion(regionNorth);
        vehicleRepository.save(v2);

        // 4. Create Drivers
        Driver d1 = new Driver();
        d1.setUser(driverUser1);
        d1.setLicenseNumber("LIC-ALX-001");
        d1.setLicenseCategory("C");
        d1.setLicenseExpiryDate(LocalDate.of(2025, 10, 10));
        d1.setDob(LocalDate.of(1990, 5, 15));
        d1.setContactNumber("555-0101");
        d1.setStatus("AVAILABLE");
        d1.setSafetyScore(new BigDecimal("98.50"));
        driverRepository.save(d1);
        
        Driver d2 = new Driver();
        d2.setUser(driverUser2);
        d2.setLicenseNumber("LIC-JHN-002");
        d2.setLicenseCategory("CE");
        d2.setLicenseExpiryDate(LocalDate.of(2026, 12, 01));
        d2.setDob(LocalDate.of(1985, 3, 22));
        d2.setContactNumber("555-0202");
        d2.setStatus("ON_TRIP");
        d2.setSafetyScore(new BigDecimal("95.00"));
        driverRepository.save(d2);

        // 5. Create Trips
        Trip t1 = new Trip();
        t1.setVehicle(v1);
        t1.setDriver(d1);
        t1.setSource("Warehouse A");
        t1.setDestination("Store B");
        t1.setExpectedDistance(new BigDecimal("120.50"));
        t1.setCargoWeight(new BigDecimal("1000.00"));
        t1.setRevenue(new BigDecimal("450.00"));
        t1.setStatus("IN_PROGRESS");
        t1.setDispatchedAt(LocalDateTime.now().minusHours(2));
        t1.setStartOdometer(new BigDecimal("12380.00"));
        tripRepository.save(t1);

        Trip t2 = new Trip();
        t2.setVehicle(v2);
        t2.setDriver(d2);
        t2.setSource("Port C");
        t2.setDestination("Distribution Center D");
        t2.setExpectedDistance(new BigDecimal("450.00"));
        t2.setCargoWeight(new BigDecimal("22000.00"));
        t2.setRevenue(new BigDecimal("1250.00"));
        t2.setStatus("COMPLETED");
        t2.setDispatchedAt(LocalDateTime.now().minusDays(1));
        t2.setCompletedAt(LocalDateTime.now().minusHours(5));
        t2.setStartOdometer(new BigDecimal("44750.75"));
        t2.setEndOdometer(new BigDecimal("45200.75"));
        tripRepository.save(t2);

        System.out.println("Mock data seeding completed successfully!");
    }
}
