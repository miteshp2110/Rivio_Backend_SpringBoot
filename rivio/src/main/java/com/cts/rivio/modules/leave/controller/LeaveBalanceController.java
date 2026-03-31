package com.cts.rivio.modules.leave.controller;

import com.cts.rivio.modules.leave.entity.EmployeeLeaveBalance;
import com.cts.rivio.modules.leave.service.LeaveBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @GetMapping("/{id}/leave-balances")
    public ResponseEntity<List<EmployeeLeaveBalance>> getEmployeeBalances(
            @PathVariable("id") Integer employeeId,
            @RequestParam(required = false) Integer year) {

        List<EmployeeLeaveBalance> balances = leaveBalanceService.getBalancesByEmployee(employeeId, year);
        return ResponseEntity.ok(balances);
    }
}