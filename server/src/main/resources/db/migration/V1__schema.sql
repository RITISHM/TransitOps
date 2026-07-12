-- Regions Table
CREATE TABLE regions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- e.g., 'MANAGER', 'DRIVER', etc.
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drivers Table
CREATE TABLE drivers (
    id SERIAL PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    dob DATE NOT NULL,
    license_number VARCHAR(100) UNIQUE NOT NULL,
    license_category VARCHAR(50) NOT NULL,
    license_expiry_date DATE NOT NULL,
    contact_number VARCHAR(50) NOT NULL,
    safety_score DECIMAL(5, 2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicles Table
CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    registration_number VARCHAR(50) UNIQUE NOT NULL,
    vehicle_name VARCHAR(255) NOT NULL,
    vehicle_type VARCHAR(100) NOT NULL,
    fuel_type VARCHAR(50) NOT NULL,
    max_load_capacity DECIMAL(10, 2) NOT NULL,
    current_odometer DECIMAL(10, 2) NOT NULL,
    acquisition_cost DECIMAL(12, 2),
    acquisition_date DATE,
    region_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trips Table
CREATE TABLE trips (
    id SERIAL PRIMARY KEY,
    vehicle_id INT NOT NULL,
    driver_id INT NOT NULL,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    expected_distance DECIMAL(10, 2),
    cargo_weight DECIMAL(10, 2),
    revenue DECIMAL(12, 2),
    dispatched_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    start_odometer DECIMAL(10, 2),
    end_odometer DECIMAL(10, 2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trip Checkpoints Table
CREATE TABLE trip_checkpoints (
    id SERIAL PRIMARY KEY,
    trip_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    checkpoint_order INT NOT NULL,
    expected_arrival TIMESTAMP,
    reached_at TIMESTAMP,
    odometer_reading_on_arrival DECIMAL(10, 2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Maintenance Logs Table
CREATE TABLE maintenance_logs (
    id SERIAL PRIMARY KEY,
    vehicle_id INT NOT NULL,
    maintenance_type VARCHAR(100) NOT NULL,
    description TEXT,
    expected_cost DECIMAL(12, 2),
    final_cost DECIMAL(12, 2),
    start_date DATE,
    end_date DATE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Fuel Logs Table
CREATE TABLE fuel_logs (
    id SERIAL PRIMARY KEY,
    vehicle_id INT NOT NULL,
    trip_id INT,
    logged_by INT NOT NULL,
    fuel_quantity DECIMAL(10, 2) NOT NULL,
    total_cost DECIMAL(12, 2) NOT NULL,
    odometer_reading DECIMAL(10, 2) NOT NULL,
    receipt_date TIMESTAMP NOT NULL,
    proof_image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Expenses Table
CREATE TABLE expenses (
    id SERIAL PRIMARY KEY,
    trip_id INT,
    vehicle_id INT,
    logged_by INT NOT NULL,
    expense_type VARCHAR(100) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    description TEXT,
    expense_date TIMESTAMP NOT NULL,
    proof_image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- Foreign Key Relationships
-- ==========================================

ALTER TABLE drivers
    ADD CONSTRAINT fk_drivers_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE vehicles
    ADD CONSTRAINT fk_vehicles_regions FOREIGN KEY (region_id) REFERENCES regions(id) ON DELETE RESTRICT;

ALTER TABLE trips
    ADD CONSTRAINT fk_trips_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE RESTRICT,
    ADD CONSTRAINT fk_trips_drivers FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE RESTRICT;

ALTER TABLE trip_checkpoints
    ADD CONSTRAINT fk_trip_checkpoints_trips FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE;

ALTER TABLE maintenance_logs
    ADD CONSTRAINT fk_maintenance_logs_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE;

ALTER TABLE fuel_logs
    ADD CONSTRAINT fk_fuel_logs_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_fuel_logs_trips FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_fuel_logs_users FOREIGN KEY (logged_by) REFERENCES users(id) ON DELETE RESTRICT;

ALTER TABLE expenses
    ADD CONSTRAINT fk_expenses_trips FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_expenses_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_expenses_users FOREIGN KEY (logged_by) REFERENCES users(id) ON DELETE RESTRICT;
