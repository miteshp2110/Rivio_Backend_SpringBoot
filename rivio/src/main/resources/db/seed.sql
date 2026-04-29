-- ==========================================
-- RIVIO HRMS - SEED DATA
-- Timeframe: Jan 1, 2026 - April 28, 2026
-- ==========================================

USE rivio;

SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------
-- 1. TRUNCATE EXISTING DATA (Idempotent seed)
-- ------------------------------------------
TRUNCATE TABLE attendance;
TRUNCATE TABLE candidates;
TRUNCATE TABLE job_openings;
TRUNCATE TABLE payslips;
TRUNCATE TABLE pay_cycles;
TRUNCATE TABLE salary_components;
TRUNCATE TABLE leave_requests;
TRUNCATE TABLE employee_leave_balances;
TRUNCATE TABLE leave_types;
TRUNCATE TABLE employee_profiles;
TRUNCATE TABLE designations;
TRUNCATE TABLE departments;
TRUNCATE TABLE locations;
TRUNCATE TABLE audit_logs;
TRUNCATE TABLE role_permissions;
TRUNCATE TABLE users;
TRUNCATE TABLE permissions;
TRUNCATE TABLE roles;
TRUNCATE TABLE holidays;
TRUNCATE TABLE work_days;

-- ------------------------------------------
-- 2. ROLES & PERMISSIONS
-- ------------------------------------------
INSERT INTO roles (id, name) VALUES 
(1, 'Super Admin'),
(2, 'Hr'),
(3, 'Manager'),
(4, 'Payroll Manager'),
(5, 'Employee');

INSERT INTO permissions (id, module, key_name) VALUES 
(1, 'System', 'MANAGE_SYSTEM_SETTINGS'),
(2, 'Employee', 'ADD_EMPLOYEE'),
(3, 'Employee', 'EDIT_EMPLOYEE'),
(4, 'Leave', 'APPROVE_LEAVE_ALL'),
(5, 'Leave', 'APPROVE_LEAVE_TEAM'),
(6, 'Payroll', 'RUN_PAYROLL'),
(7, 'Payroll', 'VIEW_ALL_PAYSLIPS'),
(8, 'Attendance', 'EDIT_ATTENDANCE_ALL'),
(9, 'Self_Service', 'VIEW_OWN_PROFILE');

-- Map Permissions to Roles
INSERT INTO role_permissions (role_id, permission_id) VALUES 
-- Super Admin (Gets everything)
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9),
-- HR
(2, 2), (2, 3), (2, 4), (2, 8), (2, 9),
-- Manager (Can approve team leaves)
(3, 5), (3, 9),
-- Payroll Manager
(4, 6), (4, 7), (4, 9),
-- Employee (Self Service only)
(5, 9);

-- ------------------------------------------
-- 3. WORK DAYS, HOLIDAYS & LEAVE TYPES
-- ------------------------------------------
-- Note: Inserting leave types BEFORE employee profiles so your TRIGGER works!
INSERT INTO leave_types (id, name, yearly_allotment, carry_forward_limit) VALUES 
(1, 'Sick Leave', 12.00, 0.00),
(2, 'Casual Leave', 12.00, 0.00),
(3, 'Earned Leave', 15.00, 15.00);

INSERT INTO work_days (id, day_name, is_working_day) VALUES 
(1, 'Monday', TRUE),
(2, 'Tuesday', TRUE),
(3, 'Wednesday', TRUE),
(4, 'Thursday', TRUE),
(5, 'Friday', TRUE),
(6, 'Saturday', FALSE),
(7, 'Sunday', FALSE);

INSERT INTO holidays (date, name) VALUES 
('2026-01-01', 'New Year''s Day'),
('2026-01-14', 'Pongal'),
('2026-01-26', 'Republic Day'),
('2026-04-03', 'Good Friday'),
('2026-04-14', 'Tamil New Year');

-- ------------------------------------------
-- 4. ORGANIZATION STRUCTURE
-- ------------------------------------------
INSERT INTO locations (id, name, currency_code, timezone) VALUES 
(1, 'Chennai HQ', 'INR', 'Asia/Kolkata'),
(2, 'Bangalore Hub', 'INR', 'Asia/Kolkata');

-- Dummy users first so departments can have a manager
INSERT INTO users (id, email, password_hash, role_id, status) VALUES 
(1, 'admin@rivio.com', '$2a$10$W70BYJ3uETsWMr4Otne.MuMCQPAVcwmEcBvNqYBOOeQOGv1EiGCH6', 1, 'ACTIVE'),
(2, 'hr@rivio.com', '$2a$10$W70BYJ3uETsWMr4Otne.MuMCQPAVcwmEcBvNqYBOOeQOGv1EiGCH6', 2, 'ACTIVE'),
(3, 'manager@rivio.com', '$2a$10$W70BYJ3uETsWMr4Otne.MuMCQPAVcwmEcBvNqYBOOeQOGv1EiGCH6', 3, 'ACTIVE'),
(4, 'payroll@rivio.com', '$2a$10$W70BYJ3uETsWMr4Otne.MuMCQPAVcwmEcBvNqYBOOeQOGv1EiGCH6', 4, 'ACTIVE'),
(5, 'employee@rivio.com', '$2a$10$W70BYJ3uETsWMr4Otne.MuMCQPAVcwmEcBvNqYBOOeQOGv1EiGCH6', 5, 'ACTIVE');

INSERT INTO departments (id, name, manager_user_id) VALUES 
(1, 'Administration', 1),
(2, 'Human Resources', 2),
(3, 'Engineering', 3),
(4, 'Finance', NULL);

INSERT INTO designations (id, title, department_id) VALUES 
(1, 'System Administrator', 1),
(2, 'HR Manager', 2),
(3, 'Engineering Lead', 3),
(4, 'Software Developer', 3),
(5, 'Payroll Specialist', 4);

-- ------------------------------------------
-- 5. EMPLOYEE PROFILES (Will fire leave trigger)
-- ------------------------------------------
INSERT INTO employee_profiles 
(id, user_id, employee_code, first_name, last_name, bank_account, phone_no, location_id, department_id, designation_id, reports_to_profile_id, employment_type, status, joining_date) 
VALUES 
-- Admin
(1, 1, 'EMP001', 'Arjun', 'Kumar', 'AC1234567890', '9876543210', 1, 1, 1, NULL, 'FULL_TIME', 'ACTIVE', '2025-01-01'),
-- HR
(2, 2, 'EMP002', 'Priya', 'Rajan', 'AC2345678901', '9876543211', 1, 2, 2, 1, 'FULL_TIME', 'ACTIVE', '2025-02-15'),
-- Manager
(3, 3, 'EMP003', 'Vikram', 'Singh', 'AC3456789012', '9876543212', 1, 3, 3, 1, 'FULL_TIME', 'ACTIVE', '2025-03-01'),
-- Payroll
(4, 4, 'EMP004', 'Anita', 'Desai', 'AC4567890123', '9876543213', 2, 4, 5, 1, 'FULL_TIME', 'ACTIVE', '2025-04-10'),
-- Employee
(5, 5, 'EMP005', 'Rahul', 'Verma', 'AC5678901234', '9876543214', 1, 3, 4, 3, 'FULL_TIME', 'ACTIVE', '2025-05-20');

-- ------------------------------------------
-- 6. SALARY COMPONENTS & PAY CYCLES
-- ------------------------------------------
-- Every employee gets a basic and HRA component
INSERT INTO salary_components (employee_profile_id, name, type, value) VALUES 
(1, 'Basic Pay', 'EARNING', 100000.00), (1, 'HRA', 'EARNING', 40000.00),
(2, 'Basic Pay', 'EARNING', 60000.00),  (2, 'HRA', 'EARNING', 24000.00),
(3, 'Basic Pay', 'EARNING', 120000.00), (3, 'HRA', 'EARNING', 48000.00),
(4, 'Basic Pay', 'EARNING', 50000.00),  (4, 'HRA', 'EARNING', 20000.00),
(5, 'Basic Pay', 'EARNING', 40000.00),  (5, 'HRA', 'EARNING', 16000.00);

-- Pay cycles for Jan, Feb, Mar, Apr (All left as DRAFT, no payslips generated)
INSERT INTO pay_cycles (id, cycle_name, from_date, to_date, status) VALUES 
(1, 'January 2026', '2026-01-01', '2026-01-31', 'DRAFT'),
(2, 'February 2026', '2026-02-01', '2026-02-28', 'DRAFT'),
(3, 'March 2026', '2026-03-01', '2026-03-31', 'DRAFT'),
(4, 'April 2026', '2026-04-01', '2026-04-30', 'DRAFT');

-- ------------------------------------------
-- 7. ATTENDANCE SEEDER (Jan 1, 2026 to Apr 28, 2026)
-- ------------------------------------------
DELIMITER //

CREATE PROCEDURE SeedAttendanceData()
BEGIN
    DECLARE current_date_val DATE DEFAULT '2026-01-01';
    DECLARE end_date_val DATE DEFAULT '2026-04-28';
    DECLARE is_weekend BOOLEAN;
    DECLARE is_holiday BOOLEAN;

    WHILE current_date_val <= end_date_val DO
        -- 1 = Sunday, 7 = Saturday in MySQL DAYOFWEEK()
        SET is_weekend = (DAYOFWEEK(current_date_val) IN (1, 7));
        
        -- Check if current day is in the holidays table
        SELECT COUNT(*) > 0 INTO is_holiday FROM holidays WHERE date = current_date_val;

        IF NOT is_weekend AND NOT is_holiday THEN
            -- Standard Working Day (Everyone is PRESENT)
            INSERT INTO attendance (employee_profile_id, date, punch_in, punch_out, status, created_by_user_id)
            SELECT id, current_date_val, CONCAT(current_date_val, ' 09:00:00'), CONCAT(current_date_val, ' 18:00:00'), 'PRESENT', 1
            FROM employee_profiles;
            
        ELSEIF NOT is_weekend AND is_holiday THEN
            -- Weekday Holiday (Everyone marked as HOLIDAY)
            INSERT INTO attendance (employee_profile_id, date, status, created_by_user_id)
            SELECT id, current_date_val, 'HOLIDAY', 1
            FROM employee_profiles;
        END IF;

        -- Move to next day
        SET current_date_val = DATE_ADD(current_date_val, INTERVAL 1 DAY);
    END WHILE;
END //

DELIMITER ;

-- Execute the seeder to populate the rows
CALL SeedAttendanceData();

-- Clean up the stored procedure as it's no longer needed
DROP PROCEDURE SeedAttendanceData;

SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- END OF SEED FILE
-- ==========================================