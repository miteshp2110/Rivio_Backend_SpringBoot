package com.cts.rivio.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkDayResponse {
    private Integer id;
    private String dayName;
    private Boolean isWorkingDay;
}