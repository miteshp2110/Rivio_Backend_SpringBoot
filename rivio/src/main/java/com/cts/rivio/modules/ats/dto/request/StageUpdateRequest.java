package com.cts.rivio.modules.ats.dto.request;

import com.cts.rivio.modules.ats.enums.CandidateStage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StageUpdateRequest {
    @NotNull(message = "Candidate stage is required")
    private CandidateStage stage;
}