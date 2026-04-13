package com.cts.rivio.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String stage;
    private String jobTitle;      // Flat field
    private String departmentName; // Flat field
}