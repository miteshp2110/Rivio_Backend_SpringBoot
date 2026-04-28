package com.cts.rivio.repository;

import com.cts.rivio.entity.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {
    List<PaySlip> findByPayCycleId(Integer payCycleId);

    @Query("SELECT p FROM PaySlip p " +
            "JOIN FETCH p.payCycle " +
            "JOIN FETCH p.employeeProfile " +
            "WHERE p.employeeProfile.id = :employeeProfileId")
    List<PaySlip> findByEmployeeProfileId(@Param("employeeProfileId") Integer employeeProfileId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PaySlip p WHERE p.payCycle.id = :payCycleId")
    void deleteByPayCycleId(Integer payCycleId);

    Optional<PaySlip> findByPayCycleIdAndEmployeeProfileId(Integer payCycleId, Integer employeeProfileId);
}