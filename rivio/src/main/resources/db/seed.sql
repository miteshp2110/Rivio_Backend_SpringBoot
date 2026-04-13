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
(1, 3, 'RIV-001', 'John', 'Doe', 'ACCT123456', '9876543210', 1, 1, 1, NULL, 'FULL_TIME', 'ACTIVE', '2020-01-15'),
(2, 2, 'RIV-002', 'Sarah', 'Connor', 'ACCT654321', '9876543211', 1, 2, 4, NULL, 'FULL_TIME', 'ACTIVE', '2021-03-01'),
(3, 4, 'RIV-003', 'Alice', 'Smith', 'ACCT111222', '9876543212', 1, 1, 2, 1, 'FULL_TIME', 'ACTIVE', '2023-06-10'),
(4, 5, 'RIV-004', 'Bob', 'Marley', 'ACCT333444', '9876543213', 2, 1, 3, 1, 'CONTRACT', 'ACTIVE', '2025-01-20');

-- ==========================================
-- 5. SALARY COMPONENTS
-- ==========================================

INSERT INTO salary_components (id, employee_profile_id, name, type, value) VALUES
-- John (VP)
(1, 1, 'Basic Pay', 'EARNING', 100000.00),
(2, 1, 'House Rent Allowance', 'EARNING', 40000.00),
(3, 1, 'Provident Fund', 'DEDUCTION', 1800.00),
-- Sarah (HR Director)
(4, 2, 'Basic Pay', 'EARNING', 80000.00),
(5, 2, 'House Rent Allowance', 'EARNING', 30000.00),
(6, 2, 'Provident Fund', 'DEDUCTION', 1800.00),
-- Alice (Senior Engineer)
(7, 3, 'Basic Pay', 'EARNING', 60000.00),
(8, 3, 'House Rent Allowance', 'EARNING', 25000.00),
(9, 3, 'Provident Fund', 'DEDUCTION', 1800.00),
-- Bob (Contract Developer)
(10, 4, 'Consolidated Pay', 'EARNING', 50000.00);

-- ==========================================
-- 6. LEAVES & PAYROLL
-- ==========================================

INSERT INTO employee_leave_balances
(employee_profile_id, leave_type_id, year, allotted, consumed) VALUES
(3, 1, 2026, 12.00, 2.00),
(3, 2, 2026, 10.00, 0.00),
(4, 1, 2026, 12.00, 0.00);

INSERT INTO leave_requests
(id, employee_profile_id, leave_type_id, start_date, end_date, days_requested, status, approved_by_profile_id) VALUES
(1, 3, 1, '2026-03-10', '2026-03-11', 2.0, 'APPROVED', 1),
(2, 4, 2, '2026-04-01', '2026-04-02', 2.0, 'PENDING', NULL);

-- UPDATED: Added the `status` column to match the new schema
INSERT INTO payslips
(id, pay_cycle_id, employee_profile_id, gross_earnings, total_deductions, net_pay, status) VALUES
(1, 1, 1, 140000.00, 1800.00, 138200.00, 'PAID'),
(2, 1, 3, 85000.00, 1800.00, 83200.00, 'PAID');

-- ==========================================
-- 7. ATS & AUDIT LOGS
-- ==========================================

INSERT INTO job_openings (id, department_id, location_id, title, status) VALUES
(1, 1, 1, 'Lead Backend Engineer', 'OPEN'),
(2, 3, 2, 'Regional Sales Manager', 'ON_HOLD');

INSERT INTO candidates
(id, job_opening_id, name, email, resume_url, stage) VALUES
(1, 1, 'Charlie Brown', 'charlie@example.com', 'https://rivio.com/resumes/charlie.pdf', 'INTERVIEWING'),
(2, 1, 'Diana Prince', 'diana@example.com', 'https://rivio.com/resumes/diana.pdf', 'OFFERED');

INSERT INTO audit_logs
(id, table_name, record_id, action, old_data, new_data, changed_by_user_id) VALUES
(1, 'departments', 1, 'UPDATE', '{"manager_user_id": null}', '{"manager_user_id": 3}', 1);

-- ==========================================
-- 8. ATTENDANCE (JANUARY & MARCH)
-- ==========================================

INSERT INTO attendance (employee_profile_id, date, punch_in, punch_out, status, created_by_user_id) VALUES
-- WEEK 1 (JAN)
(1, '2026-01-01', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-01', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-01', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-01', NULL, NULL, 'HOLIDAY', 1),
(1, '2026-01-02', '2026-01-02 09:05:00', '2026-01-02 18:15:00', 'PRESENT', 1),
(2, '2026-01-02', '2026-01-02 08:50:00', '2026-01-02 17:55:00', 'PRESENT', 1),
(3, '2026-01-02', '2026-01-02 09:10:00', '2026-01-02 18:00:00', 'PRESENT', 1),
(4, '2026-01-02', '2026-01-02 09:30:00', '2026-01-02 18:30:00', 'PRESENT', 1),
(1, '2026-01-03', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-03', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-03', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-03', NULL, NULL, 'HOLIDAY', 1),
(1, '2026-01-04', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-04', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-04', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-04', NULL, NULL, 'HOLIDAY', 1),

-- WEEK 2 (JAN)
(1, '2026-01-05', '2026-01-05 09:02:00', '2026-01-05 18:10:00', 'PRESENT', 1),
(2, '2026-01-05', '2026-01-05 08:45:00', '2026-01-05 18:00:00', 'PRESENT', 1),
(3, '2026-01-05', '2026-01-05 09:15:00', '2026-01-05 18:05:00', 'PRESENT', 1),
(4, '2026-01-05', '2026-01-05 09:25:00', '2026-01-05 18:35:00', 'PRESENT', 1),
(1, '2026-01-06', '2026-01-06 09:00:00', '2026-01-06 18:05:00', 'PRESENT', 1),
(2, '2026-01-06', '2026-01-06 08:55:00', '2026-01-06 17:50:00', 'PRESENT', 1),
(3, '2026-01-06', '2026-01-06 09:12:00', '2026-01-06 18:00:00', 'PRESENT', 1),
(4, '2026-01-06', '2026-01-06 09:30:00', '2026-01-06 18:40:00', 'PRESENT', 1),
(1, '2026-01-07', '2026-01-07 09:05:00', '2026-01-07 18:20:00', 'PRESENT', 1),
(2, '2026-01-07', '2026-01-07 08:50:00', '2026-01-07 18:10:00', 'PRESENT', 1),
(3, '2026-01-07', '2026-01-07 09:10:00', '2026-01-07 18:15:00', 'PRESENT', 1),
(4, '2026-01-07', '2026-01-07 09:35:00', '2026-01-07 18:30:00', 'PRESENT', 1),
(1, '2026-01-08', '2026-01-08 09:00:00', '2026-01-08 18:15:00', 'PRESENT', 1),
(2, '2026-01-08', '2026-01-08 08:55:00', '2026-01-08 17:55:00', 'PRESENT', 1),
(3, '2026-01-08', '2026-01-08 09:08:00', '2026-01-08 18:00:00', 'PRESENT', 1),
(4, '2026-01-08', '2026-01-08 09:30:00', '2026-01-08 18:45:00', 'PRESENT', 1),
(1, '2026-01-09', '2026-01-09 09:05:00', '2026-01-09 18:05:00', 'PRESENT', 1),
(2, '2026-01-09', '2026-01-09 08:50:00', '2026-01-09 17:50:00', 'PRESENT', 1),
(3, '2026-01-09', '2026-01-09 09:15:00', '2026-01-09 18:10:00', 'PRESENT', 1),
(4, '2026-01-09', '2026-01-09 09:40:00', '2026-01-09 18:30:00', 'PRESENT', 1),
(1, '2026-01-10', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-10', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-10', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-10', NULL, NULL, 'HOLIDAY', 1),
(1, '2026-01-11', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-11', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-11', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-11', NULL, NULL, 'HOLIDAY', 1),

-- WEEK 3 (JAN)
(1, '2026-01-12', '2026-01-12 09:05:00', '2026-01-12 18:15:00', 'PRESENT', 1),
(2, '2026-01-12', '2026-01-12 08:45:00', '2026-01-12 17:55:00', 'PRESENT', 1),
(3, '2026-01-12', '2026-01-12 09:10:00', '2026-01-12 18:00:00', 'PRESENT', 1),
(4, '2026-01-12', '2026-01-12 09:30:00', '2026-01-12 18:30:00', 'PRESENT', 1),
(1, '2026-01-13', '2026-01-13 09:00:00', '2026-01-13 18:20:00', 'PRESENT', 1),
(2, '2026-01-13', '2026-01-13 08:50:00', '2026-01-13 18:05:00', 'PRESENT', 1),
(3, '2026-01-13', '2026-01-13 09:15:00', '2026-01-13 18:10:00', 'PRESENT', 1),
(4, '2026-01-13', '2026-01-13 09:35:00', '2026-01-13 18:40:00', 'PRESENT', 1),
(1, '2026-01-14', '2026-01-14 09:05:00', '2026-01-14 18:10:00', 'PRESENT', 1),
(2, '2026-01-14', '2026-01-14 08:55:00', '2026-01-14 17:50:00', 'PRESENT', 1),
(3, '2026-01-14', NULL, NULL, 'LEAVE', 1),
(4, '2026-01-14', '2026-01-14 09:30:00', '2026-01-14 18:35:00', 'PRESENT', 1),
(1, '2026-01-15', '2026-01-15 09:02:00', '2026-01-15 18:15:00', 'PRESENT', 1),
(2, '2026-01-15', '2026-01-15 08:45:00', '2026-01-15 17:55:00', 'PRESENT', 1),
(3, '2026-01-15', '2026-01-15 09:12:00', '2026-01-15 18:05:00', 'PRESENT', 1),
(4, '2026-01-15', '2026-01-15 09:25:00', '2026-01-15 18:30:00', 'PRESENT', 1),
(1, '2026-01-16', '2026-01-16 09:05:00', '2026-01-16 18:00:00', 'PRESENT', 1),
(2, '2026-01-16', '2026-01-16 08:50:00', '2026-01-16 17:45:00', 'PRESENT', 1),
(3, '2026-01-16', '2026-01-16 09:10:00', '2026-01-16 18:00:00', 'PRESENT', 1),
(4, '2026-01-16', '2026-01-16 09:30:00', '2026-01-16 18:20:00', 'PRESENT', 1),
(1, '2026-01-17', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-17', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-17', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-17', NULL, NULL, 'HOLIDAY', 1),
(1, '2026-01-18', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-18', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-18', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-18', NULL, NULL, 'HOLIDAY', 1),

-- WEEK 4 (JAN)
(1, '2026-01-19', '2026-01-19 09:00:00', '2026-01-19 18:15:00', 'PRESENT', 1),
(2, '2026-01-19', '2026-01-19 08:55:00', '2026-01-19 17:55:00', 'PRESENT', 1),
(3, '2026-01-19', '2026-01-19 09:15:00', '2026-01-19 18:10:00', 'PRESENT', 1),
(4, '2026-01-19', '2026-01-19 09:35:00', '2026-01-19 18:40:00', 'PRESENT', 1),
(1, '2026-01-20', '2026-01-20 09:05:00', '2026-01-20 18:10:00', 'PRESENT', 1),
(2, '2026-01-20', '2026-01-20 08:50:00', '2026-01-20 18:00:00', 'PRESENT', 1),
(3, '2026-01-20', '2026-01-20 09:10:00', '2026-01-20 18:05:00', 'PRESENT', 1),
(4, '2026-01-20', '2026-01-20 09:30:00', '2026-01-20 18:35:00', 'PRESENT', 1),
(1, '2026-01-21', '2026-01-21 09:02:00', '2026-01-21 18:20:00', 'PRESENT', 1),
(2, '2026-01-21', '2026-01-21 08:45:00', '2026-01-21 17:50:00', 'PRESENT', 1),
(3, '2026-01-21', '2026-01-21 09:08:00', '2026-01-21 18:15:00', 'PRESENT', 1),
(4, '2026-01-21', '2026-01-21 09:25:00', '2026-01-21 18:30:00', 'PRESENT', 1),
(1, '2026-01-22', '2026-01-22 09:00:00', '2026-01-22 18:15:00', 'PRESENT', 1),
(2, '2026-01-22', '2026-01-22 08:55:00', '2026-01-22 17:55:00', 'PRESENT', 1),
(3, '2026-01-22', '2026-01-22 09:12:00', '2026-01-22 18:00:00', 'PRESENT', 1),
(4, '2026-01-22', '2026-01-22 09:35:00', '2026-01-22 18:45:00', 'PRESENT', 1),
(1, '2026-01-23', '2026-01-23 09:05:00', '2026-01-23 18:05:00', 'PRESENT', 1),
(2, '2026-01-23', '2026-01-23 08:50:00', '2026-01-23 17:50:00', 'PRESENT', 1),
(3, '2026-01-23', '2026-01-23 09:10:00', '2026-01-23 18:05:00', 'PRESENT', 1),
(4, '2026-01-23', '2026-01-23 09:30:00', '2026-01-23 18:30:00', 'PRESENT', 1),
(1, '2026-01-24', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-24', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-24', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-24', NULL, NULL, 'HOLIDAY', 1),
(1, '2026-01-25', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-25', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-25', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-25', NULL, NULL, 'HOLIDAY', 1),

-- WEEK 5 (JAN)
(1, '2026-01-26', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-26', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-26', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-26', NULL, NULL, 'HOLIDAY', 1),
(1, '2026-01-27', '2026-01-27 09:05:00', '2026-01-27 18:15:00', 'PRESENT', 1),
(2, '2026-01-27', '2026-01-27 08:50:00', '2026-01-27 17:55:00', 'PRESENT', 1),
(3, '2026-01-27', '2026-01-27 09:10:00', '2026-01-27 18:00:00', 'PRESENT', 1),
(4, '2026-01-27', '2026-01-27 09:30:00', '2026-01-27 18:30:00', 'PRESENT', 1),
(1, '2026-01-28', '2026-01-28 09:00:00', '2026-01-28 18:10:00', 'PRESENT', 1),
(2, '2026-01-28', '2026-01-28 08:45:00', '2026-01-28 18:05:00', 'PRESENT', 1),
(3, '2026-01-28', '2026-01-28 09:15:00', '2026-01-28 18:10:00', 'PRESENT', 1),
(4, '2026-01-28', '2026-01-28 09:25:00', '2026-01-28 18:35:00', 'PRESENT', 1),
(1, '2026-01-29', '2026-01-29 09:02:00', '2026-01-29 18:20:00', 'PRESENT', 1),
(2, '2026-01-29', '2026-01-29 08:55:00', '2026-01-29 17:50:00', 'PRESENT', 1),
(3, '2026-01-29', '2026-01-29 09:12:00', '2026-01-29 18:05:00', 'PRESENT', 1),
(4, '2026-01-29', '2026-01-29 09:35:00', '2026-01-29 18:40:00', 'PRESENT', 1),
(1, '2026-01-30', '2026-01-30 09:05:00', '2026-01-30 18:05:00', 'PRESENT', 1),
(2, '2026-01-30', '2026-01-30 08:50:00', '2026-01-30 17:45:00', 'PRESENT', 1),
(3, '2026-01-30', '2026-01-30 09:10:00', '2026-01-30 18:00:00', 'PRESENT', 1),
(4, '2026-01-30', '2026-01-30 09:30:00', '2026-01-30 18:20:00', 'PRESENT', 1),
(1, '2026-01-31', NULL, NULL, 'HOLIDAY', 1),
(2, '2026-01-31', NULL, NULL, 'HOLIDAY', 1),
(3, '2026-01-31', NULL, NULL, 'HOLIDAY', 1),
(4, '2026-01-31', NULL, NULL, 'HOLIDAY', 1),

-- MARCH
(1, '2026-03-27', '2026-03-27 09:00:00', '2026-03-27 18:00:00', 'PRESENT', 1),
(3, '2026-03-27', '2026-03-27 09:15:00', NULL, 'PRESENT', 1),
(4, '2026-03-27', NULL, NULL, 'LEAVE', 1);