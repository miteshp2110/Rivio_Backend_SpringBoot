package com.cts.rivio.modules;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/health")
    public String checkHealth(){
        return "All Systems are working fine";
    }

}
