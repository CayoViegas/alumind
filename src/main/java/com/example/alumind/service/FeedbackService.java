package com.example.alumind.service;

import com.example.alumind.model.Feedback;
import com.example.alumind.model.FeedbackResponse;
import com.example.alumind.model.RequestedFeature;
import com.example.alumind.repository.FeedbackRepository;
import com.example.alumind.repository.FeedbackResponseRepository;
import com.example.alumind.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
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

    public FeedbackResponse processFeedback(Feedback feedback) {
        String userPrompt = String.format("Analise o feedback a seguir quanto ao sentimento e as sugestões de melhorias:\n\nFeedback: %s", feedback.getFeedback());
        String systemPrompt = Utils.loadSystemPrompt();
        Double temperature = 0.6;

        GroqService.FeedbackFormat feedbackFormat = groqService.getChatResponse("llama-3.1-70b-versatile", userPrompt, systemPrompt, temperature);

        if (feedbackFormat.getSentiment().equalsIgnoreCase("SPAM")) {
            return new FeedbackResponse("SPAM", new ArrayList<>());
        }

        List<RequestedFeature> requestedFeatures = feedbackFormat.getRequestedFeatures();

        if (requestedFeatures == null) {
            requestedFeatures = new ArrayList<>();
        }

        FeedbackResponse feedbackResponse = new FeedbackResponse(feedbackFormat.getSentiment(), requestedFeatures);
        FeedbackResponse savedResponse = feedbackResponseRepository.save(feedbackResponse);

        feedback.setFeedbackResponse(savedResponse);
        feedbackRepository.save(feedback);

        return savedResponse;

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
