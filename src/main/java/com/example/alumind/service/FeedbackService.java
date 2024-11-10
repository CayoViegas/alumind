package com.example.alumind.service;

import com.example.alumind.model.Feedback;
import com.example.alumind.model.FeedbackResponse;
import com.example.alumind.model.RequestedFeature;
import com.example.alumind.repository.FeedbackRepository;
import com.example.alumind.repository.FeedbackResponseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackResponseRepository feedbackResponseRepository;

    @Autowired
    private GroqService groqService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FeedbackResponse processFeedback(Feedback feedback) {
        String userPrompt = String.format("Analise o feedback a seguir quanto ao sentimento e as sugestões de melhorias:\n\nFeedback: %s", feedback.getFeedback());

        String jsonResponse = groqService.getChatResponse("llama-3.1-70b-versatile", userPrompt);

        System.out.println(jsonResponse);

        try {
            String content = extractContentField(jsonResponse);

            String jsonExtract = extractJsonFromContent(content);

            if (jsonExtract == null) {
                throw new RuntimeException("Failed to extract JSON from GroqService content field");
            }

            JsonNode rootNode = objectMapper.readTree(jsonExtract);
            String sentiment = rootNode.path("sentiment").asText();
            List<RequestedFeature> requestedFeatures = new ArrayList<>();

            JsonNode featuresNode = rootNode.path("requested_features");

            if (featuresNode.isArray()) {
                for (JsonNode featureNode : featuresNode) {
                    String code = featureNode.path("code").asText();
                    String reason = featureNode.path("reason").asText();

                    if (!code.isEmpty() && !reason.isEmpty()) {
                        requestedFeatures.add(new RequestedFeature(code, reason));
                    }
                }
            }

            FeedbackResponse feedbackResponse = new FeedbackResponse(sentiment, requestedFeatures);
            FeedbackResponse savedResponse = feedbackResponseRepository.save(feedbackResponse);

            feedback.setFeedbackResponse(savedResponse);
            feedbackRepository.save(feedback);

            return savedResponse;
        } catch (IOException e) {
            throw new RuntimeException("Error processing feedback response from GroqService", e);
        }
    }

    private String extractContentField(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        return rootNode.path("choices").get(0).path("message").path("content").asText();
    }

    private String extractJsonFromContent(String content) {
        int startIndex = content.indexOf("json\n{");

        if (startIndex == -1) {
            return null;
        }

        startIndex += 5;

        int openBraces = 1;
        int endIndex = startIndex + 1;

        while (endIndex < content.length() && openBraces > 0) {
            String currentChar = content.substring(endIndex, endIndex + 1);

            if (currentChar.equals("{")) {
                openBraces++;
            } else if (currentChar.equals("}")) {
                openBraces--;
            }

            endIndex++;
        }

        if (openBraces != 0) {
            throw new RuntimeException("JSON block is not properly closed in the response.");
        }

        return content.substring(startIndex - 1, endIndex).trim();
    }
}
