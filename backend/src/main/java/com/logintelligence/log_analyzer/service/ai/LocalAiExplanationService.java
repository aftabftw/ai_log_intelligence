package com.logintelligence.log_analyzer.service.ai;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class LocalAiExplanationService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:11434")
            .clientConnector(
                    new ReactorClientHttpConnector(
                            HttpClient.create()
                                    .responseTimeout(Duration.ofMinutes(2))
                    )
            )
            .build();

    public String explainError(String errorMessage) {

        Map<String, Object> request = Map.of(
                "model", "mistral",
                "prompt", "Explain this software error and suggest fix: " + errorMessage,
                "stream", false
        );

        return webClient.post()
                .uri("/api/generate")
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> response.get("response").toString())
                .block();
    }

}

