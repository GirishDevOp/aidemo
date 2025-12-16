package com.aidmeo.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.aidmeo.api.dto.ChatResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final WebClient aiClient;

    @Value("${euri.api.key}")
    private String apiKey;

    @Value("${euri.model}")
    private String model;
    
    public ChatResponse getChatResponse(String userMessage) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", 1000,
                "temperature", 0.7,
                "messages", new Object[]{
                        Map.of("role", "user", "content", userMessage)
                }
        );

        String reply = aiClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (java.util.List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return (String) message.get("content");
                    }
                    return "No response from AI.";
                })
                .block();

        return new ChatResponse(reply);
    }
}
