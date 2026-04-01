package com.cts.rivio.modules.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data // 3. This tells Lombok to create the getDate() and getName() methods for you.
public class HolidayRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Holiday name is required")
    private String name;
}