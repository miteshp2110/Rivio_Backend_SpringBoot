package com.cts.rivio.dto.request;

import com.cts.rivio.enums.CandidateStage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StageUpdateRequest {
    @NotNull(message = "Candidate stage is required")
    private CandidateStage stage;
}