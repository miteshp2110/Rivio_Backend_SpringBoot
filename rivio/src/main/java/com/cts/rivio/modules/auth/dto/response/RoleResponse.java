package com.cts.rivio.modules.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {
    private Integer id;
    private String name;
}