package com.example.alumind.service;

import com.example.alumind.model.RequestedFeature;
import com.example.alumind.util.GroqHelper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Service
public class GroqService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiToken;

    @Autowired
    private GroqHelper groqHelper;

    public GroqService(RestTemplate restTemplate,
                       @Value("${spring.ai.openai.base-url}") String apiUrl,
                       @Value("${spring.ai.openai.api-key}") String apiToken) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
    }

    public FeedbackFormat getChatResponse(String model, String content, String systemPrompt, Double temperature) {
        String url = apiUrl + "/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        ChatRequest request = new ChatRequest(model, content, systemPrompt, 0.6);
        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
        System.out.println(entity.getBody());

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(response.getBody());
        return parseResponse(response.getBody());
    }

    private FeedbackFormat parseResponse(String responseBody) {
        try {
            return groqHelper.parseResponse(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing response from GroqService", e);
        }
    }

    private static class ChatRequest {
        private String model;
        private Message[] messages;
        private Double temperature;
        private ResponseFormat responseFormat;

        public ChatRequest(String model, String content, String systemPrompt, Double temperature) {
            this.model = model;
            this.messages = new Message[]{
                    new Message("system", systemPrompt),
                    new Message("user", content)
            };
            this.temperature = temperature;
            this.responseFormat = new ResponseFormat("json_object");
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeedbackFormat {
        @JsonProperty("sentiment")
        private String sentiment;

        @JsonProperty("requested_features")
        private List<RequestedFeature> requestedFeatures;

        @JsonProperty("response_message")
        private String responseMessage;

        public String getSentiment() {
            return sentiment;
        }

        public List<RequestedFeature> getRequestedFeatures() {
            return requestedFeatures;
        }

        public String getResponseMessage() {
            return responseMessage;
        }
    }

    public static class ResponseFormat {
        private String type;

        public ResponseFormat(String type) {
            this.type = type;
        }

        public String getType() { return type; }
        public void setType(String value) { this.type = value; }
    }
}
