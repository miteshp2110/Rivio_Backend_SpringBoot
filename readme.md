# Rivio HRMS — API Documentation

**Version:** 1.0  
**Base URL:** `http://localhost:8081/api`  
**Total API Endpoints:** 72  
**Content-Type:** `application/json` (unless specified otherwise)

---

## Table of Contents

1. [Global Standards](#global-standards)
2. [Authentication & Users](#1-authentication--users) — 5 endpoints
3. [Roles & Permissions](#2-roles--permissions) — 7 endpoints
4. [Company Structure](#3-company-structure) — 12 endpoints
5. [Employee Management](#4-employee-management) — 7 endpoints
6. [Leave Management](#5-leave-management) — 10 endpoints
7. [Attendance & Scheduling](#6-attendance--scheduling) — 11 endpoints
8. [Applicant Tracking System](#7-applicant-tracking-system-ats) — 10 endpoints
9. [Payroll Management](#8-payroll-management) — 10 endpoints
10. [Dashboard Analytics](#9-dashboard-analytics) — 2 endpoints

---

## Global Standards

### Authentication

All protected routes require a JWT token passed via the `Authorization` header:

```
Authorization: Bearer <your_token_here>
```

### Standard Response Wrapper

Every API response (except file downloads) follows this structure:

```json
{
  "success": true,
  "message": "Dynamic message",
  "data": { ... },
  "timestamp": "2026-04-16T10:00:00"
}
```

> **Note:** All "Response Body" examples in this document represent the contents of the `data` field inside this wrapper.

### Pagination Format

Paginated endpoints accept these query params: `page` (default 0), `size` (default 10), `sortBy` (default `id`), `sortDir` (`asc` / `desc`).

Paginated responses follow this shape:

```json
{
  "content": [ ... ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 25,
  "totalPages": 3,
  "last": false
}
```

---

## 1. Authentication & Users

### 1.1 Login

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/auth/login` |

**Request:**

```json
{
  "email": "admin@rivio.com",
  "password": "securePassword123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "role": "Super Admin",
  "permissions": ["ALL_ACCESS", "VIEW_PROFILE"],
  "employeeProfileId": 10,
  "name": "Mitesh Paliwal"
}
```

### 1.2 Create User

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/users` |

**Request:**

```json
{
  "email": "mitesh@rivio.com",
  "password": "Password123!",
  "roleId": 2
}
```

**Response:**

```json
{
  "id": 5,
  "email": "mitesh@rivio.com",
  "roleName": "HR Manager",
  "status": "ACTIVE"
}
```

### 1.3 Get All Users (Paginated)

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/users` |
| **Query Params** | `page`, `size`, `sortBy`, `sortDir` |

**Response:** Paginated wrapper containing user objects with fields `id`, `email`, `roleName`, `status`.

### 1.4 Reset User Password

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/users/{id}/reset-password` |

**Request:**

```json
{ "newPassword": "NewSecurePass123!" }
```

**Response:** `null` — check the `message` field in the wrapper.

### 1.5 Delete User

| | |
|---|---|
| **Method** | `DELETE` |
| **URL** | `/users/{id}` |

Hard deletes if unused; soft-deletes (suspends) if linked to an employee profile. Response is `null` — check the `message` field.

---

## 2. Roles & Permissions

### 2.1 Get All Permissions

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/permissions` |

**Response:**

```json
[
  { "id": 1, "module": "System", "keyName": "ALL_ACCESS", "description": null }
]
```

### 2.2 Get All Roles

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/roles` |

**Response:**

```json
[{ "id": 1, "name": "Super Admin" }]
```

### 2.3 Get Role by ID

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/roles/{id}` |

**Response:**

```json
{ "id": 1, "name": "Super Admin" }
```

### 2.4 Create Role

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/roles` |

**Request / Response:**

```json
{ "name": "IT Support" }
→ { "id": 5, "name": "IT Support" }
```

### 2.5 Update Role

| | |
|---|---|
| **Method** | `PUT` |
| **URL** | `/roles/{id}` |

**Request / Response:**

```json
{ "name": "IT Admin" }
→ { "id": 5, "name": "IT Admin" }
```

### 2.6 Get Permissions for Role

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/roles/{id}/permissions` |

**Response:**

```json
[{ "id": 2, "module": "Leave", "keyName": "APPROVE_LEAVE" }]
```

### 2.7 Bind Permissions to Role

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/roles/{id}/permissions` |

Completely **replaces** existing permissions for the role.

**Request:**

```json
[1, 2, 5]
```

**Response:** `null`

---

## 3. Company Structure

### 3.1 Locations

| Method | URL | Body |
|--------|-----|------|
| `GET` | `/locations` | — |
| `POST` | `/locations` | `{ "name", "currencyCode", "timezone" }` |
| `PUT` | `/locations/{id}` | Same as POST |
| `DELETE` | `/locations/{id}` | — |

**Sample Response (GET):**

```json
[{ "id": 1, "name": "Bengaluru HQ", "currencyCode": "INR", "timezone": "Asia/Kolkata" }]
```

### 3.2 Departments

| Method | URL | Body |
|--------|-----|------|
| `GET` | `/departments` | — |
| `POST` | `/departments` | `{ "name", "managerUserId" }` |
| `PUT` | `/departments/{id}` | Same as POST |
| `DELETE` | `/departments/{id}` | — |

**Sample Response (GET):**

```json
[{ "id": 1, "name": "Engineering", "managerUserId": 3, "managerEmail": "john@rivio.com" }]
```

### 3.3 Designations

| Method | URL | Body |
|--------|-----|------|
| `GET` | `/designations` | — (optional `?departmentId=1`) |
| `POST` | `/designations` | `{ "title", "departmentId" }` |
| `PUT` | `/designations/{id}` | Same as POST |
| `DELETE` | `/designations/{id}` | — |

**Sample Response (GET):**

```json
[{ "id": 1, "title": "Backend Developer", "departmentId": 1, "departmentName": "Engineering" }]
```

---

## 4. Employee Management

### 4.1 Onboard Employee

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/employees` |

**Request:**

```json
{
  "userId": 5,
  "employeeCode": "2200033062",
  "firstName": "Mitesh",
  "lastName": "Paliwal",
  "departmentId": 1,
  "designationId": 2,
  "locationId": 1,
  "reportsToProfileId": null,
  "joiningDate": "2026-01-15",
  "employmentType": "FULL_TIME"
}
```

### 4.2 Get Employee Profile

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/employees/{id}` |
| **Headers** | `X-Role` (optional) |

**Response:**

```json
{
  "id": 10,
  "employeeCode": "2200033062",
  "firstName": "Mitesh",
  "lastName": "Paliwal",
  "userEmail": "mitesh@rivio.com",
  "departmentName": "Engineering",
  "designationTitle": "Backend Developer",
  "locationName": "Bengaluru HQ",
  "managerName": "John Doe",
  "employmentType": "FULL_TIME",
  "status": "ACTIVE",
  "joiningDate": "2026-01-15",
  "phoneNo": "9876543210",
  "bankAccount": "ACCT123",
  "salaryComponents": [ ... ]
}
```

### 4.3 Get Employee Directory (Paginated)

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/employees` |
| **Query Params** | `search` (name or code), `page`, `size` |

Returns paginated objects with `id`, `employeeCode`, `firstName`, `lastName`, `email`, `departmentName`, `designationTitle`.

### 4.4 Get Employees Eligible for Attendance

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/employees/eligible-for-attendance` |
| **Query Params** | `date` (YYYY-MM-DD, defaults to today) |

**Response:**

```json
[{ "id": 10, "name": "Mitesh Paliwal" }]
```

### 4.5 Update Job Details (Promotion / Transfer)

| | |
|---|---|
| **Method** | `PUT` |
| **URL** | `/employees/{id}/job-details` |
| **Headers** | `X-User-Id` |

**Request (all fields optional):**

```json
{
  "departmentId": 2,
  "designationId": 4,
  "locationId": 1,
  "reportsToProfileId": 3
}
```

### 4.6 Update Basic Info

| | |
|---|---|
| **Method** | `PATCH` |
| **URL** | `/employees/{id}/basic-info` |

**Request:**

```json
{ "phoneNo": "9998887776", "bankAccount": "HDFC0001" }
```

### 4.7 Manage Employee Status (Termination)

| | |
|---|---|
| **Method** | `PUT` |
| **URL** | `/employees/{id}/status` |

**Request:**

```json
{
  "status": "TERMINATED",
  "exitDate": "2026-05-01",
  "overridePastDate": false
}
```

---

## 5. Leave Management

### 5.1 Leave Types (Settings)

| Method | URL | Body |
|--------|-----|------|
| `GET` | `/leave-types` | — |
| `POST` | `/leave-types` | `{ "name", "yearlyAllotment", "carryForwardLimit" }` |
| `PUT` | `/leave-types/{id}` | Same as POST |
| `DELETE` | `/leave-types/{id}` | — |

**Sample Response (GET):**

```json
[{ "id": 1, "name": "Sick Leave", "yearlyAllotment": 12.0, "carryForwardLimit": 0.0 }]
```

### 5.2 Leave Balances

**Allocate Balances (Batch):**

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/leave-balances/allocate` |

```json
{ "year": 2026 }
```

**Get Employee Balances:**

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/employees/{id}/leave-balances` |
| **Query Params** | `year` (optional) |

```json
[{ "id": 1, "leaveTypeName": "Sick Leave", "year": 2026, "allotted": 12.0, "consumed": 2.0, "balance": 10.0 }]
```

### 5.3 Leave Requests

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/employees/{id}/leave-requests` | Fetch history |
| `POST` | `/leave-requests` | Apply for leave |
| `PUT` | `/leave-status-updates/{id}` | Approve / Reject (header: `X-Manager-Id`) |
| `DELETE` | `/leave-requests/{id}` | Withdraw pending request |

**Apply Request Body:**

```json
{
  "employeeProfileId": 10,
  "leaveTypeId": 1,
  "startDate": "2026-04-10",
  "endDate": "2026-04-12",
  "daysRequested": 3.0
}
```

**Approve / Reject Body:**

```json
{ "status": "APPROVED" }
```

---

## 6. Attendance & Scheduling

### 6.1 Attendance

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/attendance?date=YYYY-MM-DD` | Org-wide attendance for a day |
| `GET` | `/attendance/employee/{id}/history` | Employee history (`startDate`, `endDate`) |
| `POST` | `/attendance` | Manual Punch In (header: `X-User-Id`) |
| `PATCH` | `/attendance/{id}/punch-out` | Manual Punch Out |

**Punch In Body:**

```json
{
  "employeeProfileId": 10,
  "date": "2026-04-16",
  "punchIn": "2026-04-16T09:00:00",
  "punchOut": null
}
```

**Punch Out Body:**

```json
{ "punchOut": "2026-04-16T18:00:00" }
```

### 6.2 Bulk Upload Attendance

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/attendance/upload/template` | Download CSV template |
| `POST` | `/attendance/upload` | Upload CSV (form-data, key: `file`) |

**Upload Response:**

```json
{
  "totalRecords": 50,
  "successfulRecords": 48,
  "failedRecords": 2,
  "errors": ["Row 3 failed: Invalid Employee ID"]
}
```

### 6.3 Work Days & Holidays

| Method | URL | Body |
|--------|-----|------|
| `GET` | `/work-days` | — |
| `PUT` | `/work-days/{id}` | `{ "isWorkingDay": false }` |
| `GET` | `/holidays` | — |
| `POST` | `/holidays` | `{ "date": "2026-11-08", "name": "Diwali" }` |
| `DELETE` | `/holidays/{id}` | — |

---

## 7. Applicant Tracking System (ATS)

### 7.1 Job Openings

| Method | URL | Description |
|--------|-----|-------------|
| `POST` | `/job-openings` | Create opening |
| `GET` | `/job-openings` | List all |
| `GET` | `/job-openings/{id}` | Get by ID |
| `PATCH` | `/job-openings/{id}/status` | Update status |
| `DELETE` | `/job-openings/{id}` | Delete |

**Create Body:**

```json
{ "title": "React Developer", "departmentId": 1, "locationId": 1 }
```

**Status Update Body:**

```json
{ "status": "CLOSED" }
```

### 7.2 Candidates

| Method | URL | Description |
|--------|-----|-------------|
| `POST` | `/job-openings/{id}/candidates` | Apply |
| `GET` | `/job-openings/{id}/candidates` | List (optional `?stage=INTERVIEWING`) |
| `GET` | `/candidates/{id}` | Get candidate |
| `PUT` | `/candidates/{id}/stage` | Move stage |
| `POST` | `/candidates/{id}/hire` | Convert to employee |

**Apply Body:**

```json
{ "name": "Alice Johnson", "email": "alice@email.com", "resumeUrl": "aws.s3/resume.pdf" }
```

**Stage Update Body:**

```json
{ "stage": "OFFERED" }
```

**Candidate Response:**

```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice@email.com",
  "resumeUrl": "...",
  "stage": "APPLIED",
  "jobOpeningId": 1,
  "jobOpeningTitle": "React Developer"
}
```

---

## 8. Payroll Management

### 8.1 Salary Components

| Method | URL | Body |
|--------|-----|------|
| `GET` | `/salary-components/employee/{id}` | — |
| `POST` | `/salary-components/employee/{id}` | `{ "name", "type", "value" }` |
| `PUT` | `/salary-components/{id}` | Same as POST |
| `DELETE` | `/salary-components/{id}` | — |

**Create Body:**

```json
{ "name": "Basic Pay", "type": "EARNING", "value": 50000.00 }
```

### 8.2 Pay Cycles

| Method | URL | Description |
|--------|-----|-------------|
| `POST` | `/pay-cycles` | Create cycle |
| `GET` | `/pay-cycles` | List (optional `?name=April`) |
| `PUT` | `/pay-cycles/{id}/status` | Finalize cycle |
| `POST` | `/pay-cycles/{id}/generate-payslips` | Trigger payslip generation |

**Create Body:**

```json
{ "cycleName": "April 2026", "fromDate": "2026-04-01", "toDate": "2026-04-30" }
```

**Finalize Body:**

```json
{ "status": "FINALIZED" }
```

### 8.3 Payslips

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/pay-cycles/{id}/payslips` | All slips for a cycle |
| `GET` | `/pay-cycles/{id}/payslips/employee/{employeeId}` | Specific employee's slip |

**Response:**

```json
{
  "id": 1,
  "payCycleId": 1,
  "employeeProfileId": 10,
  "employeeName": "Mitesh Paliwal",
  "employeeCode": "2200033062",
  "grossEarnings": 60000.00,
  "totalDeductions": 1800.00,
  "netPay": 58200.00
}
```

---

## 9. Dashboard Analytics

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/dashboard/admin-summary` | Admin KPIs |
| `GET` | `/health` | System health check |

**Admin Summary Response:**

```json
{
  "totalActiveEmployees": 45,
  "totalOpenJobs": 3,
  "pendingLeaveRequests": 12,
  "totalCandidates": 105
}
```

**Health Response:**

```json
{ "success": true, "message": "All Systems are healthy", "data": null }
```

---

## API Count Summary

| Module | Endpoints |
|--------|-----------|
| Authentication & Users | 5 |
| Roles & Permissions | 7 |
| Company Structure (Locations + Departments + Designations) | 12 |
| Employee Management | 7 |
| Leave Management (Types + Balances + Requests) | 10 |
| Attendance & Scheduling (Attendance + Upload + Work Days + Holidays) | 11 |
| Applicant Tracking System (Openings + Candidates) | 10 |
| Payroll Management (Components + Cycles + Payslips) | 10 |
| Dashboard Analytics | 2 |
| **Total** | **72** |

---

*Generated for Rivio HRMS v1.0*
