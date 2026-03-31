package com.cts.rivio.modules.leave.controller;

import com.cts.rivio.modules.leave.service.LeaveAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/leave-balances")
public class LeaveAllocationController {

    @Autowired
    private LeaveAllocationService allocationService;

    @PostMapping("/allocate")
    public ResponseEntity<?> runAllocation(@RequestBody(required = false) Map<String, Integer> request) {
        // Default to current year if not provided in JSON body
        Integer year = (request != null && request.containsKey("year"))
                ? request.get("year")
                : LocalDate.now().getYear();

        allocationService.allocateBalancesForYear(year);
        return ResponseEntity.ok("Leave balances allocated successfully for year: " + year);
    }
}