package com.cts.rivio.dto.response;

import com.cts.rivio.enums.CandidateStage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateDTO {
    private Integer id;
    private String name;
    private String email;
    private String resumeUrl;
    private CandidateStage stage;

    // Flattened Job Opening details so the UI knows which job they applied for
    private Integer jobOpeningId;
    private String jobOpeningTitle;
}