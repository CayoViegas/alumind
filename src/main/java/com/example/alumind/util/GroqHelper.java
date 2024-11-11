package com.example.alumind.util;

import com.example.alumind.service.GroqService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GroqHelper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GroqService.FeedbackFormat parseResponse(String responseBody) throws IOException{

        JsonNode rootNode = objectMapper.readTree(responseBody);
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();

        int jsonStart = content.indexOf("{");
        int jsonEnd = content.lastIndexOf("}");

        if (jsonStart == -1 || jsonEnd == -1) {
            throw new RuntimeException("No JSON content found in response");
        }

        String jsonExtract = content.substring(jsonStart, jsonEnd + 1);

        return objectMapper.readValue(jsonExtract, GroqService.FeedbackFormat.class);
    }
}
