package com.cts.rivio.service;

import com.cts.rivio.dto.response.PaySlipResponse;
import java.util.List;

public interface PayrollService {
    List<PaySlipResponse> getPayslipsByEmployeeId(Integer employeeId);
    List<PaySlipResponse> generatePayslipsForCycle(Integer payCycleId);
    List<PaySlipResponse> getPayslipsByPayCycle(Integer payCycleId);
    PaySlipResponse getPayslipByEmployeeAndPayCycle(Integer payCycleId, Integer employeeId);
}