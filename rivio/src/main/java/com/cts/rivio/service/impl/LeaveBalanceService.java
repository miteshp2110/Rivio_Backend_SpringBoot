package com.cts.rivio.service.impl;

import com.cts.rivio.entity.EmployeeLeaveBalance;
import com.cts.rivio.repository.EmployeeLeaveBalanceRepository;
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

        // FIX: Change 'year' to 'targetYear' in the repository call
        return balanceRepository.findByEmployeeProfileIdAndYear(employeeId, targetYear);

    }
}