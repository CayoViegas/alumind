package com.example.alumind.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GroqService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiToken;
    private final String systemPrompt;

    public GroqService(RestTemplate restTemplate,
                       @Value("${spring.ai.openai.base-url}") String apiUrl,
                       @Value("${spring.ai.openai.api-key}") String apiToken) throws IOException {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.systemPrompt = loadSystemPrompt();
    }

    public String getChatResponse(String model, String content) {
        String url = apiUrl + "/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        ChatRequest request = new ChatRequest(model, content, systemPrompt);
        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response.getBody();
    }

    private static class ChatRequest {
        private String model;
        private Message[] messages;

        public ChatRequest(String model, String content, String systemPrompt) {
            this.model = model;
            this.messages = new Message[]{
                    new Message("system", systemPrompt),
                    new Message("user", content)
            };
        }

        private static class Message {
            private String role;
            private String content;

            public Message(String role, String content) {
                this.role = role;
                this.content = content;
            }

            public String getRole() {
                return role;
            }

            public String getContent() {
                return content;
            }
        }

        public String getModel() {
            return model;
        }

        public Message[] getMessages() {
            return messages;
        }
    }

    private String loadSystemPrompt() throws IOException {
        Path path = ResourceUtils.getFile("classpath:prompts/SYSTEM_PROMPT.txt").toPath();
        return Files.readString(path);
    }
}
