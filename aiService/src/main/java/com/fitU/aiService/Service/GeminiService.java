package com.fitU.aiService.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service

public class GeminiService {

    @Autowired
    private WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;



    public String GetRecommendation(String details){
        Map<String, Object> body = Map.of(
                "contents", new Object[]{   // ✅ correct key
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", details)
                                }
                        )
                }
        );

        String response= webClient.post()
                .uri(geminiUrl)
                .header("Content-Type", "application/json")
                .header("X-goog-api-key",apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }
}
