package com.cts.rivio.modules.auth.dto.response;


import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AuthResponse {
    private String token;
    private Integer userId;
    private String role;
    private List<String> permissions;
}
