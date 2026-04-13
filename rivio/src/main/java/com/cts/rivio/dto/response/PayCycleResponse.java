package com.cts.rivio.dto.response;

import com.cts.rivio.enums.PayCycleStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PayCycleResponse {
    private Integer id;
    private String cycleName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private PayCycleStatus status;
}