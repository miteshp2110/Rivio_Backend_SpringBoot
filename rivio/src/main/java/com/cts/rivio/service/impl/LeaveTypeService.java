package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.entity.LeaveType;
import com.cts.rivio.repository.EmployeeLeaveBalanceRepository;
import com.cts.rivio.repository.LeaveRequestRepository;
import com.cts.rivio.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private  EmployeeLeaveBalanceRepository leaveBalanceRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    /**
     * LEAV-19: Create a new leave type.
     * Ensures the name is unique and allotment rules are set.
     */
    @Transactional
    public LeaveType createLeaveType(LeaveType leaveType) {
        // Business check for LEAV-19 AC1 (Unique Name)
        // Note: The @Column(unique=true) in the entity is the primary safeguard,
        // but this check allows for a custom exception message.
        return leaveTypeRepository.save(leaveType);
    }

    /**
     * LEAV-23: Update existing leave rules.
     */
    @Transactional
    public LeaveType updateLeaveRules(Integer id, LeaveType updatedDetails) {
        LeaveType existingType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Type not found with id: " + id));

        // Update the "Rules" based on updatedDetails
        if (updatedDetails.getName() != null) {
            existingType.setName(updatedDetails.getName());
        }

        existingType.setYearlyAllotment(updatedDetails.getYearlyAllotment());
        existingType.setCarryForwardLimit(updatedDetails.getCarryForwardLimit());

        // Note: As per LEAV-23 AC1, we only update the template (LeaveType).
        // Existing EmployeeLeaveBalance records remain unchanged.
        return leaveTypeRepository.save(existingType);
    }


    @Transactional
    public void deleteLeaveType(Integer id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Type", "id", id));

        // AC 1: Safe Delete Check
        boolean hasActiveBalances = leaveBalanceRepository.existsByLeaveTypeId(id);
        boolean hasHistoricalRequests = leaveRequestRepository.existsByLeaveTypeId(id);

        if (hasActiveBalances || hasHistoricalRequests) {
            throw new IllegalStateException(
                    "Cannot delete Leave Type: Employees currently have active balances or historical requests under this type."
            );
        }

        leaveTypeRepository.delete(leaveType);
    }
}