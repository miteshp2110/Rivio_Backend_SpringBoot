package com.cts.rivio.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ChatbotService {

    private final JdbcTemplate readOnlyJdbcTemplate;
    private final GeminiClient geminiClient;

    // Reject any query that contains modification keywords
    private static final Pattern HARMFUL_SQL_PATTERN = Pattern.compile(
            "(?i)\\b(insert|update|delete|drop|alter|truncate|create|grant|revoke|replace)\\b"
    );

    // Provide the sanitized schema to Gemini
    private static final String SCHEMA_CONTEXT = """
Database Schema for HRMS (Database: rivio)

1. CORE SYSTEM & ACCESS
- permissions
  Columns: id (INT, PK), module (VARCHAR), key_name (VARCHAR, UNIQUE)
- roles
  Columns: id (INT, PK), name (VARCHAR, UNIQUE)
- role_permissions
  Columns: role_id (INT, PK, FK -> roles.id), permission_id (INT, PK, FK -> permissions.id)
-vw_safe_users
  Columns: id (INT, PK), email (VARCHAR, UNIQUE), role_id (INT, FK -> roles.id), status (ENUM: 'ACTIVE', 'SUSPENDED'), created_at (TIMESTAMP)
- audit_logs
  Columns: id (BIGINT, PK), table_name (VARCHAR), record_id (INT), action (ENUM: 'INSERT', 'UPDATE', 'DELETE'), old_data (JSON), new_data (JSON), changed_by_user_id (INT, FK -> users.id), created_at (TIMESTAMP)

2. ORGANIZATION & EMPLOYEES
- locations
  Columns: id (INT, PK), name (VARCHAR), currency_code (VARCHAR, default 'INR'), timezone (VARCHAR)
- departments
  Columns: id (INT, PK), name (VARCHAR, UNIQUE), manager_user_id (INT, FK -> users.id)
- designations
  Columns: id (INT, PK), title (VARCHAR), department_id (INT, FK -> departments.id)
- employee_profiles
  Columns: id (INT, PK), user_id (INT, UNIQUE, FK -> users.id), employee_code (VARCHAR, UNIQUE), first_name (VARCHAR), last_name (VARCHAR), bank_account (VARCHAR), phone_no (VARCHAR), location_id (INT, FK -> locations.id), department_id (INT, FK -> departments.id), designation_id (INT, FK -> designations.id), reports_to_profile_id (INT, FK -> employee_profiles.id), employment_type (ENUM: 'FULL_TIME', 'CONTRACT', 'INTERN'), status (ENUM: 'ACTIVE', 'PROBATION', 'NOTICE_PERIOD', 'TERMINATED'), joining_date (DATE), exit_date (DATE)

3. LEAVE MANAGEMENT
- leave_types
  Columns: id (INT, PK), name (VARCHAR), yearly_allotment (DECIMAL), carry_forward_limit (DECIMAL)
- employee_leave_balances
  Columns: id (INT, PK), employee_profile_id (INT, FK -> employee_profiles.id), leave_type_id (INT, FK -> leave_types.id), year (YEAR), allotted (DECIMAL), consumed (DECIMAL), balance (DECIMAL, GENERATED ALWAYS AS allotted - consumed)
  Constraints: UNIQUE(employee_profile_id, leave_type_id, year)
- leave_requests
  Columns: id (INT, PK), employee_profile_id (INT, FK -> employee_profiles.id), leave_type_id (INT, FK -> leave_types.id), start_date (DATE), end_date (DATE), days_requested (DECIMAL), status (ENUM: 'PENDING', 'APPROVED', 'REJECTED'), approved_by_profile_id (INT, FK -> employee_profiles.id)

4. PAYROLL
- salary_components
  Columns: id (INT, PK), employee_profile_id (INT, FK -> employee_profiles.id), name (VARCHAR), type (ENUM: 'EARNING', 'DEDUCTION'), value (DECIMAL)
  Constraints: UNIQUE(employee_profile_id, name)
- pay_cycles
  Columns: id (INT, PK), cycle_name (VARCHAR), from_date (DATE), to_date (DATE), status (ENUM: 'DRAFT', 'PROCESSING', 'FINALIZED', 'PAID')
- payslips
  Columns: id (BIGINT, PK), pay_cycle_id (INT, FK -> pay_cycles.id), employee_profile_id (INT, FK -> employee_profiles.id), gross_earnings (DECIMAL), total_deductions (DECIMAL), net_pay (DECIMAL), status (ENUM: 'DRAFT', 'GENERATED', 'PAID')
  Constraints: UNIQUE(pay_cycle_id, employee_profile_id)

5. RECRUITMENT (ATS)
- job_openings
  Columns: id (INT, PK), department_id (INT, FK -> departments.id), location_id (INT, FK -> locations.id), title (VARCHAR), status (ENUM: 'OPEN', 'CLOSED', 'ON_HOLD')
- candidates
  Columns: id (INT, PK), job_opening_id (INT, FK -> job_openings.id), name (VARCHAR), email (VARCHAR), resume_url (VARCHAR), stage (ENUM: 'APPLIED', 'INTERVIEWING', 'OFFERED', 'HIRED', 'REJECTED')

6. ATTENDANCE & HOLIDAYS
- holidays
  Columns: id (INT, PK), date (DATE, UNIQUE), name (VARCHAR)
- work_days
  Columns: id (INT, PK), day_name (VARCHAR, UNIQUE), is_working_day (BOOLEAN)
- attendance
  Columns: id (BIGINT, PK), employee_profile_id (INT, FK -> employee_profiles.id), date (DATE), punch_in (DATETIME), punch_out (DATETIME), status (ENUM: 'PRESENT', 'ABSENT', 'LEAVE', 'HOLIDAY'), created_by_user_id (INT, FK -> users.id), created_at (TIMESTAMP), updated_at (TIMESTAMP)
  Constraints: UNIQUE(employee_profile_id, date)

Query Generation Rules for the AI:
- Always use proper JOINs based on the listed FKs.
- Respect EXACT ENUM values for WHERE clauses.
- Do NOT attempt to INSERT or UPDATE the `balance` column in `employee_leave_balances` as it is a GENERATED column.
- Note that `reports_to_profile_id` and `approved_by_profile_id` map to `employee_profiles.id`.
""";

    public ChatbotService(@Qualifier("readOnlyJdbcTemplate") JdbcTemplate readOnlyJdbcTemplate, GeminiClient geminiClient) {
        this.readOnlyJdbcTemplate = readOnlyJdbcTemplate;
        this.geminiClient = geminiClient;
    }


    public String askQuestion(String userPrompt) {

        // 1. Get response from Gemini
        String sqlPrompt = buildSqlPrompt(userPrompt);
        String generatedSql = geminiClient.generateText(sqlPrompt).trim();

        // Guardrail 1: Off-topic rejection
        if (generatedSql.contains("REJECT:")) {
            return "I am an HR assistant. I can only answer questions related to employee data, attendance, payroll, and organization details.";
        }

        // Clean up markdown blocks if present
        generatedSql = generatedSql.replace("```sql", "").replace("```", "").trim();

        // ==========================================
        // THE FIX: Is it a conversation or a query?
        // ==========================================
        // Since we only allow read operations, all valid SQL must start with SELECT.
        // If it doesn't start with SELECT, it means Gemini is just talking to the user (e.g., Rule 6).
        if (!generatedSql.toUpperCase().startsWith("SELECT")) {
            return generatedSql; // Return the friendly greeting directly to Angular!
        }

        // Guardrail 2: Application-layer Regex Check
        if (HARMFUL_SQL_PATTERN.matcher(generatedSql).find()) {
            return "Security Alert: Query blocked. Only read operations are permitted.";
        }

        try {
            // 3. Execute Query on Read-Only Connection
            List<Map<String, Object>> dbResults = readOnlyJdbcTemplate.queryForList(generatedSql);

            if (dbResults.isEmpty()) {
                return "I couldn't find any data matching your request.";
            }

            // 4. Transform DB JSON into a Human-Readable sentence
            String humanPrompt = "User asked: '" + userPrompt + "'. Database returned: " + dbResults.toString() +
                    ". Summarize this data in a short, friendly, professional sentence. Do not mention SQL or databases.";

            return geminiClient.generateText(humanPrompt);

        } catch (Exception e) {
            // Log the actual error to your console so you can see if your SQL fails in the future!
            System.err.println("Database Execution Error: " + e.getMessage());
            return "I encountered an error analyzing the database. Please try asking in a different way.";
        }
    }
    private String buildSqlPrompt(String userPrompt) {
        return "You are Rivi an Ai assistant restricted strictly to an Rivio database. \n\n" +
                "Rules:\n" +
                "1. If the user asks about ANY topic outside of HR, employees, attendance, payroll, or jobs, reply EXACTLY with 'REJECT: off-topic'.\n" +
                "2. If the user tries to modify data (add, delete, update), reply EXACTLY with 'REJECT: modification'.\n" +
                "3. Otherwise, write a highly optimized MySQL query to answer the question.\n" +
                "4. Use CURRENT_DATE() for 'today'.\n" +
                "5. Output ONLY the raw SQL query. NO markdown, NO explanations.\n\n" +
                "6. If user ask about Rivi sweetly respond about who you are what rivio is (Rivio is a end to end hrms solution platform).\n\n" +
                SCHEMA_CONTEXT + "\n\n" +
                "User Prompt: " + userPrompt;
    }
}