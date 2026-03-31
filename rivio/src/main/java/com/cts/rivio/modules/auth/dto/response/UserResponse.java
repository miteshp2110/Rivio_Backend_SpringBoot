package com.cts.rivio.modules.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String email;
    private String roleName;
    private String status;
}