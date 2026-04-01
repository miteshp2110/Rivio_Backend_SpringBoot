package com.cts.rivio.modules.attendance.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class HolidayResponse {
    private Integer id;
    private LocalDate date;
    private String name;
}