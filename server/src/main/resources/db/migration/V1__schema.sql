-- ==========================================
-- TransitOps V1 Baseline Schema
-- Fixes applied: #5, #13, #14, #15, #16, #17
-- ==========================================

-- Regions Table
CREATE TABLE regions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL CHECK (role IN ('FLEET_MANAGER', 'DRIVER', 'SAFETY_OFFICER', 'FINANCIAL_ANALYST')),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Drivers Table
CREATE TABLE drivers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    dob DATE,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    license_category VARCHAR(20),
    license_expiry_date DATE NOT NULL,
    contact_number VARCHAR(20),
    safety_score DECIMAL(5, 2) NOT NULL DEFAULT 100.00,
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'ON_TRIP', 'OFF_DUTY', 'SUSPENDED')),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Vehicles Table
CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,
    registration_number VARCHAR(30) NOT NULL UNIQUE,
    vehicle_name VARCHAR(100),
    vehicle_type VARCHAR(20) NOT NULL CHECK (vehicle_type IN ('TRUCK', 'VAN', 'BUS', 'PICKUP', 'TRAILER', 'OTHER')),
    fuel_type VARCHAR(20),
    max_load_capacity DECIMAL(10, 2) NOT NULL,
    current_odometer DECIMAL(10, 2) NOT NULL DEFAULT 0,
    acquisition_cost DECIMAL(12, 2) NOT NULL,
    acquisition_date DATE,
    region_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'ON_TRIP', 'IN_SHOP', 'RETIRED')),
    next_maintenance_due_date DATE,
    next_maintenance_due_odometer DECIMAL(10, 2),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Trips Table
CREATE TABLE trips (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    expected_distance DECIMAL(10, 2),
    cargo_weight DECIMAL(10, 2),
    revenue DECIMAL(12, 2),
    dispatched_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason VARCHAR(255),
    start_odometer DECIMAL(10, 2),
    end_odometer DECIMAL(10, 2),
    status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'DISPATCHED', 'COMPLETED', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Trip Checkpoints Table
CREATE TABLE trip_checkpoints (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    checkpoint_order INT NOT NULL,
    expected_arrival TIMESTAMP,
    reached_at TIMESTAMP,
    odometer_reading_on_arrival DECIMAL(10, 2),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'ARRIVED', 'SKIPPED')) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Maintenance Logs Table (fix #15: added logged_by)
CREATE TABLE maintenance_logs (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    logged_by BIGINT NOT NULL,
    maintenance_type VARCHAR(50),
    description TEXT,
    expected_cost DECIMAL(12, 2),
    final_cost DECIMAL(12, 2),
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'CLOSED')),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Fuel Logs Table (fix #5: vehicle_id NOT NULL)
CREATE TABLE fuel_logs (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    trip_id BIGINT,
    logged_by BIGINT NOT NULL,
    fuel_quantity DECIMAL(10, 2) NOT NULL,
    total_cost DECIMAL(10, 2) NOT NULL,
    odometer_reading DECIMAL(10, 2) NOT NULL,
    refuel_date TIMESTAMP NOT NULL DEFAULT now(),
    proof_image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Expenses Table (fix #14: expense_type CHECK + chk_expense_has_target)
CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT,
    vehicle_id BIGINT,
    logged_by BIGINT NOT NULL,
    expense_type VARCHAR(30) NOT NULL CHECK (expense_type IN ('TOLL', 'PERMIT', 'MISC')),
    amount DECIMAL(10, 2) NOT NULL,
    description TEXT,
    expense_date TIMESTAMP NOT NULL DEFAULT now(),
    proof_image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT chk_expense_has_target CHECK (trip_id IS NOT NULL OR vehicle_id IS NOT NULL)
);

-- ==========================================
-- Foreign Key Relationships
-- ==========================================

ALTER TABLE drivers
    ADD CONSTRAINT fk_drivers_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE vehicles
    ADD CONSTRAINT fk_vehicles_regions FOREIGN KEY (region_id) REFERENCES regions(id) ON DELETE RESTRICT;

ALTER TABLE trips
    ADD CONSTRAINT fk_trips_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE RESTRICT;

ALTER TABLE trips
    ADD CONSTRAINT fk_trips_drivers FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE RESTRICT;

ALTER TABLE trip_checkpoints
    ADD CONSTRAINT fk_trip_checkpoints_trips FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE;

ALTER TABLE maintenance_logs
    ADD CONSTRAINT fk_maintenance_logs_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE;

ALTER TABLE maintenance_logs
    ADD CONSTRAINT fk_maintenance_logs_users FOREIGN KEY (logged_by) REFERENCES users(id) ON DELETE RESTRICT;

ALTER TABLE fuel_logs
    ADD CONSTRAINT fk_fuel_logs_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE;

ALTER TABLE fuel_logs
    ADD CONSTRAINT fk_fuel_logs_trips FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE SET NULL;

ALTER TABLE fuel_logs
    ADD CONSTRAINT fk_fuel_logs_users FOREIGN KEY (logged_by) REFERENCES users(id) ON DELETE RESTRICT;

ALTER TABLE expenses
    ADD CONSTRAINT fk_expenses_trips FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE SET NULL;

ALTER TABLE expenses
    ADD CONSTRAINT fk_expenses_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE SET NULL;

ALTER TABLE expenses
    ADD CONSTRAINT fk_expenses_users FOREIGN KEY (logged_by) REFERENCES users(id) ON DELETE RESTRICT;

-- ==========================================
-- Indexes for common query patterns
-- ==========================================

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_drivers_status ON drivers(status);
CREATE INDEX idx_drivers_license_expiry ON drivers(license_expiry_date);
CREATE INDEX idx_vehicles_status ON vehicles(status);
CREATE INDEX idx_vehicles_region ON vehicles(region_id);
CREATE INDEX idx_vehicles_type ON vehicles(vehicle_type);
CREATE INDEX idx_trips_status ON trips(status);
CREATE INDEX idx_trips_vehicle ON trips(vehicle_id);
CREATE INDEX idx_trips_driver ON trips(driver_id);
CREATE INDEX idx_fuel_logs_vehicle ON fuel_logs(vehicle_id);
CREATE INDEX idx_expenses_vehicle ON expenses(vehicle_id);
CREATE INDEX idx_expenses_trip ON expenses(trip_id);
CREATE INDEX idx_maintenance_vehicle ON maintenance_logs(vehicle_id);
