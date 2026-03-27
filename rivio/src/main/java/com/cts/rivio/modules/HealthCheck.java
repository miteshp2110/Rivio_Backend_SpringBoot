package com.cts.rivio.modules;


import com.cts.rivio.core.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/health")
    public ResponseEntity<ApiResponse> checkHealth(){
        return ResponseEntity.ok(ApiResponse.success(null,"All Systems are healthy"));
    }
}
