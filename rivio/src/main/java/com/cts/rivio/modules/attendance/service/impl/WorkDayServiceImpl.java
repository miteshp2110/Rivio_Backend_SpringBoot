package com.cts.rivio.modules.attendance.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.attendance.dto.request.WorkDayRequest;
import com.cts.rivio.modules.attendance.dto.response.WorkDayResponse;
import com.cts.rivio.modules.attendance.entity.WorkDay;
import com.cts.rivio.modules.attendance.mapper.WorkDayMapper;
import com.cts.rivio.modules.attendance.repository.WorkDayRepository;
import com.cts.rivio.modules.attendance.service.WorkDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkDayServiceImpl implements WorkDayService {

    private final WorkDayRepository workDayRepository;
    private final WorkDayMapper workDayMapper;

    @Override
    public List<WorkDayResponse> getAllWorkDays() {
        return workDayRepository.findAll().stream()
                .map(workDayMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkDayResponse updateWorkDay(Integer id, WorkDayRequest request) {
        WorkDay workDay = workDayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkDay", "id", id));

        workDay.setIsWorkingDay(request.getIsWorkingDay());
        return workDayMapper.toResponse(workDayRepository.save(workDay));
    }
}