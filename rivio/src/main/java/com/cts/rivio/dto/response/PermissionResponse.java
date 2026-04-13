package com.cts.rivio.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {
    private Integer id;
    private String module;
    private String keyName;
    private String description;
}