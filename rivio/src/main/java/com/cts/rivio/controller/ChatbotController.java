package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;

import com.cts.rivio.service.impl.ChatbotService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat") // If you have a global /api prefix, the full URL will be /api/chat/ask
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<String>> askBot(@RequestBody ChatRequest request) {

        // Pass the user's question to the service we built earlier
        String answer = chatbotService.askQuestion(request.getQuestion());

        // Return the standard Rivio API response
        return ResponseEntity.ok(ApiResponse.success(answer, "Chat response generated"));
    }

    // --- Simple Inner DTO for the request payload ---
    @Data
    public static class ChatRequest {
        private String question;
    }
}