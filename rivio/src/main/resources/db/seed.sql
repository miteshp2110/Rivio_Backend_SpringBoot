USE rivio;

-- ==========================================
-- 1. BASE CONFIGURATION TABLES
-- ==========================================

INSERT INTO permissions (id, module, key_name) VALUES
(1, 'System', 'ALL_ACCESS'),
(2, 'Leave', 'APPROVE_LEAVE'),
(3, 'Payroll', 'RUN_PAYROLL'),
(4, 'ATS', 'MANAGE_CANDIDATES'),
(5, 'Employee', 'VIEW_PROFILE');

INSERT INTO roles (id, name) VALUES
(1, 'Super Admin'),
(2, 'HR Manager'),
(3, 'Department Head'),
(4, 'Employee');

INSERT INTO locations (id, name, currency_code, timezone) VALUES
(1, 'Bengaluru HQ', 'INR', 'Asia/Kolkata'),
(2, 'Mumbai Branch', 'INR', 'Asia/Kolkata'),
(3, 'New York Office', 'USD', 'America/New_York');

INSERT INTO leave_types (id, name, yearly_allotment, carry_forward_limit) VALUES
(1, 'Sick Leave', 12.00, 0.00),
(2, 'Casual Leave', 10.00, 0.00),
(3, 'Earned Leave', 15.00, 5.00);

INSERT INTO salary_components (id, name, type, value) VALUES
(1, 'Basic Pay', 'EARNING', 50000.00),
(2, 'House Rent Allowance', 'EARNING', 20000.00),
(3, 'Provident Fund', 'DEDUCTION', 1800.00),
(4, 'Professional Tax', 'DEDUCTION', 200.00);

INSERT INTO pay_cycles (id, cycle_name, from_date, to_date, status) VALUES
(1, 'February 2026', '2026-02-01', '2026-02-28', 'PAID'),
(2, 'March 2026', '2026-03-01', '2026-03-31', 'PROCESSING');

INSERT INTO holidays (id, date, name) VALUES
(1, '2026-01-01', 'New Year'),
(2, '2026-01-26', 'Republic Day'),
(3, '2026-05-01', 'Labour Day');

INSERT INTO work_days (id, day_name, is_working_day) VALUES
(1, 'Monday', TRUE),
(2, 'Tuesday', TRUE),
(3, 'Wednesday', TRUE),
(4, 'Thursday', TRUE),
(5, 'Friday', TRUE),
(6, 'Saturday', FALSE),
(7, 'Sunday', FALSE);

-- ==========================================
-- 2. ROLE MAPPINGS & USERS
-- ==========================================

INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1),
(2, 2), (2, 3), (2, 4), (2, 5),
(3, 2), (3, 5),
(4, 5);

INSERT INTO users (id, email, password_hash, role_id, status) VALUES
(1, 'admin@rivio.com', SHA2('password123', 256), 1, 'ACTIVE'),
(2, 'sarah.hr@rivio.com', SHA2('password123', 256), 2, 'ACTIVE'),
(3, 'john.manager@rivio.com', SHA2('password123', 256), 3, 'ACTIVE'),
(4, 'alice.emp@rivio.com', SHA2('password123', 256), 4, 'ACTIVE'),
(5, 'bob.emp@rivio.com', SHA2('password123', 256), 4, 'ACTIVE');

-- ==========================================
-- 3. ORGANIZATION STRUCTURE
-- ==========================================

INSERT INTO departments (id, name, manager_user_id) VALUES
(1, 'Engineering', 3),
(2, 'Human Resources', 2),
(3, 'Sales', NULL);

INSERT INTO designations (id, title, department_id) VALUES
(1, 'VP of Engineering', 1),
(2, 'Senior Software Engineer', 1),
(3, 'Frontend Developer', 1),
(4, 'HR Director', 2),
(5, 'Sales Executive', 3);

-- ==========================================
-- 4. EMPLOYEE PROFILES
-- ==========================================

INSERT INTO employee_profiles (
    id, user_id, employee_code, first_name, last_name,
    bank_account, phone_no, location_id,
    department_id, designation_id, reports_to_profile_id,
    employment_type, status, joining_date
) VALUES
(1, 3, 'RIV-001', 'John', 'Doe',
 'ACCT123456', '9876543210', 1,
 1, 1, NULL,
 'FULL_TIME', 'ACTIVE', '2020-01-15'),

(2, 2, 'RIV-002', 'Sarah', 'Connor',
 'ACCT654321', '9876543211', 1,
 2, 4, NULL,
 'FULL_TIME', 'ACTIVE', '2021-03-01'),

(3, 4, 'RIV-003', 'Alice', 'Smith',
 'ACCT111222', '9876543212', 1,
 1, 2, 1,
 'FULL_TIME', 'ACTIVE', '2023-06-10'),

(4, 5, 'RIV-004', 'Bob', 'Marley',
 'ACCT333444', '9876543213', 2,
 1, 3, 1,
 'CONTRACT', 'ACTIVE', '2025-01-20');

-- ==========================================
-- 5. LEAVES, ATTENDANCE & PAYROLL
-- ==========================================

INSERT INTO employee_leave_balances
(employee_profile_id, leave_type_id, year, allotted, consumed) VALUES
(3, 1, 2026, 12.00, 2.00),
(3, 2, 2026, 10.00, 0.00),
(4, 1, 2026, 12.00, 0.00);

INSERT INTO leave_requests
(id, employee_profile_id, leave_type_id, start_date, end_date,
 days_requested, status, approved_by_profile_id) VALUES
(1, 3, 1, '2026-03-10', '2026-03-11', 2.0, 'APPROVED', 1),
(2, 4, 2, '2026-04-01', '2026-04-02', 2.0, 'PENDING', NULL);

INSERT INTO attendance
(id, employee_profile_id, date, punch_in, punch_out, status, created_by_user_id) VALUES
(1, 1, '2026-03-27', '2026-03-27 09:00:00', '2026-03-27 18:00:00', 'PRESENT', 1),
(2, 3, '2026-03-27', '2026-03-27 09:15:00', NULL, 'PRESENT', 1),
(3, 4, '2026-03-27', NULL, NULL, 'LEAVE', 1);

INSERT INTO payslips
(id, pay_cycle_id, employee_profile_id, gross_earnings, total_deductions, net_pay) VALUES
(1, 1, 1, 150000.00, 15000.00, 135000.00),
(2, 1, 3, 80000.00, 5000.00, 75000.00);

-- ==========================================
-- 6. ATS & AUDIT LOGS
-- ==========================================

INSERT INTO job_openings (id, department_id, location_id, title, status) VALUES
(1, 1, 1, 'Lead Backend Engineer', 'OPEN'),
(2, 3, 2, 'Regional Sales Manager', 'ON_HOLD');

INSERT INTO candidates
(id, job_opening_id, name, email, resume_url, stage) VALUES
(1, 1, 'Charlie Brown', 'charlie@example.com',
 'https://rivio.com/resumes/charlie.pdf', 'INTERVIEWING'),
(2, 1, 'Diana Prince', 'diana@example.com',
 'https://rivio.com/resumes/diana.pdf', 'OFFERED');

INSERT INTO audit_logs
(id, table_name, record_id, action, old_data, new_data, changed_by_user_id) VALUES
(1, 'departments', 1, 'UPDATE',
 '{"manager_user_id": null}',
 '{"manager_user_id": 3}', 1);