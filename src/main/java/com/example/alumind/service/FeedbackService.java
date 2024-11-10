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
        String userPrompt = String.format("Analyze the following feedback for sentiment and requested features:\n\nFeedback: \"%s\"\n\nReturn JSON format: {\"sentiment\": \"string\", \"requested_features\": [{\"code\": \"string\", \"reason\": \"string\"}]}", feedback.getFeedback());

        String jsonResponse = groqService.getChatResponse("llama-3.1-70b-versatile", userPrompt);

        System.out.println(jsonResponse);

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            String sentiment = rootNode.path("sentiment").asText();
            List<RequestedFeature> requestedFeatures = new ArrayList<>();

            JsonNode featuresNode = rootNode.path("requested_features");
            if (featuresNode.isArray()) {
                for (JsonNode featureNode : featuresNode) {
                    String code = featureNode.path("code").asText();
                    String reason = featureNode.path("reason").asText();
                    requestedFeatures.add(new RequestedFeature(code, reason));
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
}
