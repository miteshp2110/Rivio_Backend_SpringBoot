package com.cts.rivio.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    // Notice we removed the ?key= from the URL because we will pass it in the Header now!
    // Feel free to swap "gemini-1.5-flash" with "gemini-3-flash-preview" here.
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent";

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateText(String prompt) {

        // 1. Set Headers (Matches: -H "x-goog-api-key: $GEMINI_API_KEY" and -H "Content-Type: application/json")
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        // 2. Build the JSON Body (Matches your -d payload exactly)
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );
//        System.out.println(requestBody);

        // 3. Combine Headers and Body into a Request Entity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 4. Execute the POST request
            Map<String, Object> response = restTemplate.postForObject(GEMINI_API_URL, requestEntity, Map.class);

            // 5. Parse the response
//            System.out.println(response);
            return extractTextFromResponse(response);

        } catch (Exception e) {
            System.err.println("Gemini API Error: " + e.getMessage());
            return "Sorry, I am having trouble connecting to the AI service right now.";
        }
    }

    /**
     * Safely drills down into Gemini's JSON response to get the actual text string.
     */
    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            System.err.println("Failed to parse Gemini response: " + e.getMessage());
            return "Error parsing response.";
        }
    }
}