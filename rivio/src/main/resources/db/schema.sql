-- ==========================================
-- 1. CREATE TABLES (Without Foreign Keys)
-- ==========================================

CREATE DATABASE rivio;
USE rivio;

CREATE TABLE permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    module VARCHAR(50) NOT NULL COMMENT 'e.g., Payroll, Leave, ATS',
    key_name VARCHAR(100) NOT NULL UNIQUE COMMENT 'e.g., APPROVE_PAYROLL'
);

CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE role_permissions (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    status ENUM('ACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    record_id INT NOT NULL,
    action ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    old_data JSON,
    new_data JSON,
    changed_by_user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT 'e.g., Chennai Office',
    currency_code VARCHAR(3) NOT NULL DEFAULT 'INR',
    timezone VARCHAR(50) NOT NULL
);

CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    manager_user_id INT COMMENT 'Department Head'
);

CREATE TABLE designations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    department_id INT NOT NULL
);

CREATE TABLE employee_profiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE COMMENT 'Links to login credentials',
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    bank_account VARCHAR(20),
    phone_no VARCHAR(12),
    location_id INT NOT NULL,
    department_id INT NOT NULL,
    designation_id INT NOT NULL,
    reports_to_profile_id INT COMMENT 'Direct Manager',
    employment_type ENUM('FULL_TIME', 'CONTRACT', 'INTERN') DEFAULT 'FULL_TIME',
    status ENUM('ACTIVE', 'PROBATION', 'NOTICE_PERIOD', 'TERMINATED') DEFAULT 'ACTIVE',
    joining_date DATE NOT NULL,
    exit_date DATE
);

CREATE TABLE leave_types (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT 'Sick, Casual, Earned',
    yearly_allotment DECIMAL(5,2) NOT NULL,
    carry_forward_limit DECIMAL(5,2) DEFAULT 0.00
);

CREATE TABLE employee_leave_balances (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_profile_id INT NOT NULL,
    leave_type_id INT NOT NULL,
    year YEAR NOT NULL,
    allotted DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    consumed DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    balance DECIMAL(5,2) GENERATED ALWAYS AS (allotted - consumed) STORED,
    UNIQUE KEY (employee_profile_id, leave_type_id, year)
);

CREATE TABLE leave_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_profile_id INT NOT NULL,
    leave_type_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    days_requested DECIMAL(4,1) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    approved_by_profile_id INT
);

-- SIMPLIFIED: Employee ID is directly in the component table
CREATE TABLE salary_components (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_profile_id INT NOT NULL,
    name VARCHAR(100) NOT NULL COMMENT 'e.g., Basic, HRA, Provident Fund, TDS',
    type ENUM('EARNING', 'DEDUCTION') NOT NULL,
    value DECIMAL(10,2) NOT NULL,
    UNIQUE KEY (employee_profile_id, name) COMMENT 'Ensures an employee doesnt get duplicate Basic components'
);

CREATE TABLE pay_cycles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cycle_name VARCHAR(100) NOT NULL COMMENT 'e.g., March 2026',
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    status ENUM('DRAFT', 'PROCESSING', 'FINALIZED', 'PAID') DEFAULT 'DRAFT'
);

CREATE TABLE payslips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pay_cycle_id INT NOT NULL,
    employee_profile_id INT NOT NULL,
    gross_earnings DECIMAL(15,2) NOT NULL,
    total_deductions DECIMAL(15,2) NOT NULL,
    net_pay DECIMAL(15,2) NOT NULL,
    UNIQUE KEY (pay_cycle_id, employee_profile_id)
);

CREATE TABLE job_openings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department_id INT NOT NULL,
    location_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    status ENUM('OPEN', 'CLOSED', 'ON_HOLD') DEFAULT 'OPEN'
);

CREATE TABLE candidates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_opening_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    resume_url VARCHAR(255) NOT NULL,
    stage ENUM('APPLIED', 'INTERVIEWING', 'OFFERED', 'HIRED', 'REJECTED') DEFAULT 'APPLIED'
);

CREATE TABLE holidays (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL COMMENT 'e.g., New Year, Diwali'
);

CREATE TABLE work_days (
    id INT AUTO_INCREMENT PRIMARY KEY,
    day_name VARCHAR(20) NOT NULL UNIQUE COMMENT 'e.g., Monday, Sunday',
    is_working_day BOOLEAN DEFAULT TRUE
);

CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_profile_id INT NOT NULL,
    date DATE NOT NULL,
    punch_in DATETIME,
    punch_out DATETIME,
    status ENUM('PRESENT', 'ABSENT', 'LEAVE', 'HOLIDAY') DEFAULT 'PRESENT',
    created_by_user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY (employee_profile_id, date) COMMENT 'Prevents duplicate daily records'
);

-- ==========================================
-- 2. ADD FOREIGN KEYS
-- ==========================================

ALTER TABLE role_permissions
    ADD FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    ADD FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE;

ALTER TABLE users
    ADD FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE audit_logs
    ADD FOREIGN KEY (changed_by_user_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE departments
    ADD FOREIGN KEY (manager_user_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE designations
    ADD FOREIGN KEY (department_id) REFERENCES departments(id);

ALTER TABLE employee_profiles
    ADD FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    ADD FOREIGN KEY (location_id) REFERENCES locations(id),
    ADD FOREIGN KEY (department_id) REFERENCES departments(id),
    ADD FOREIGN KEY (designation_id) REFERENCES designations(id),
    ADD FOREIGN KEY (reports_to_profile_id) REFERENCES employee_profiles(id) ON DELETE SET NULL;

ALTER TABLE employee_leave_balances
    ADD FOREIGN KEY (employee_profile_id) REFERENCES employee_profiles(id) ON DELETE CASCADE,
    ADD FOREIGN KEY (leave_type_id) REFERENCES leave_types(id);

ALTER TABLE leave_requests
    ADD FOREIGN KEY (employee_profile_id) REFERENCES employee_profiles(id) ON DELETE CASCADE,
    ADD FOREIGN KEY (leave_type_id) REFERENCES leave_types(id),
    ADD FOREIGN KEY (approved_by_profile_id) REFERENCES employee_profiles(id) ON DELETE SET NULL;

-- NEW FOREIGN KEY linking salary components directly to the employee
ALTER TABLE salary_components
    ADD FOREIGN KEY (employee_profile_id) REFERENCES employee_profiles(id) ON DELETE CASCADE;

ALTER TABLE payslips
    ADD FOREIGN KEY (pay_cycle_id) REFERENCES pay_cycles(id),
    ADD FOREIGN KEY (employee_profile_id) REFERENCES employee_profiles(id);

ALTER TABLE job_openings
    ADD FOREIGN KEY (department_id) REFERENCES departments(id),
    ADD FOREIGN KEY (location_id) REFERENCES locations(id);

ALTER TABLE candidates
    ADD FOREIGN KEY (job_opening_id) REFERENCES job_openings(id) ON DELETE CASCADE;

ALTER TABLE attendance
    ADD FOREIGN KEY (employee_profile_id) REFERENCES employee_profiles(id) ON DELETE CASCADE,
    ADD FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON DELETE SET NULL;