package com.cts.rivio.modules.attendance.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AttendanceResponse {
    private Long id;
    private String employeeName;
    private LocalDate date;
    private LocalDateTime punchIn;
    private LocalDateTime punchOut;
    private String status;
}