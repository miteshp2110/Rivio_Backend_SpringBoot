package com.cts.rivio.modules.leave.service;

import com.cts.rivio.modules.leave.entity.EmployeeLeaveBalance;
import com.cts.rivio.modules.leave.repository.EmployeeLeaveBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveBalanceService {

    @Autowired
    private EmployeeLeaveBalanceRepository balanceRepository;

    public List<EmployeeLeaveBalance> getBalancesByEmployee(Integer employeeId, Integer year) {
        // Default to current year if not specified
        int targetYear = (year != null) ? year : LocalDate.now().getYear();
// New/Correct call matching the Repository
        return balanceRepository.findByEmployeeProfileIdAndYear(employeeId, year);
    }
}