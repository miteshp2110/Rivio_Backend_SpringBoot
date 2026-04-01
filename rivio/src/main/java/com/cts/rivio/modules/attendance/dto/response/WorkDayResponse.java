package com.cts.rivio.modules.attendance.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkDayResponse {
    private Integer id;
    private String dayName;
    private Boolean isWorkingDay;
}