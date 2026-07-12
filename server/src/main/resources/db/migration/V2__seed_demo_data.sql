-- ==========================================
-- TransitOps V2 Seed Demo Data
-- Adds 1 Region, 4 Users (one per role), 1 Driver record linked to the driver user
-- Password for all users: 'password123'
-- Hash generated via BCrypt: $2a$10$eOBy0G4l1J6Ea70w20K.UeG8Xj.4O8g4.t83p.r9v0B4.7sO7/X4m
-- ==========================================

-- Insert Region
INSERT INTO regions (name, code)
VALUES ('North America Region 1', 'NA-1');

-- Insert Users
INSERT INTO users (name, email, password_hash, role)
VALUES 
('Alice FleetManager', 'manager@transitops.com', '$2a$10$eOBy0G4l1J6Ea70w20K.UeG8Xj.4O8g4.t83p.r9v0B4.7sO7/X4m', 'FLEET_MANAGER'),
('Bob Driver', 'driver@transitops.com', '$2a$10$eOBy0G4l1J6Ea70w20K.UeG8Xj.4O8g4.t83p.r9v0B4.7sO7/X4m', 'DRIVER'),
('Charlie Safety', 'safety@transitops.com', '$2a$10$eOBy0G4l1J6Ea70w20K.UeG8Xj.4O8g4.t83p.r9v0B4.7sO7/X4m', 'SAFETY_OFFICER'),
('Diana Finance', 'finance@transitops.com', '$2a$10$eOBy0G4l1J6Ea70w20K.UeG8Xj.4O8g4.t83p.r9v0B4.7sO7/X4m', 'FINANCIAL_ANALYST');

-- Insert Driver linked to 'Bob Driver'
INSERT INTO drivers (user_id, dob, license_number, license_category, license_expiry_date, contact_number, status)
SELECT id, '1985-05-15', 'DL-NA1-12345', 'CDL-A', '2028-12-31', '555-0101', 'AVAILABLE'
FROM users
WHERE email = 'driver@transitops.com';
