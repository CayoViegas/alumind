package com.example.alumind.service;

import com.example.alumind.model.Feedback;
import com.example.alumind.model.FeedbackResponse;
import com.example.alumind.model.RequestedFeature;
import com.example.alumind.repository.FeedbackRepository;
import com.example.alumind.repository.FeedbackResponseRepository;
import com.example.alumind.util.FeedbackAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackResponseRepository feedbackResponseRepository;

    public FeedbackResponse processFeedback(Feedback feedback) {
        String sentiment = FeedbackAnalysisUtil.analyzeSentiment(feedback.getFeedback());
        List<RequestedFeature> requestedFeatures = FeedbackAnalysisUtil.extractRequestedFeatures(feedback.getFeedback());

        FeedbackResponse feedbackResponse = new FeedbackResponse(sentiment, requestedFeatures);
        FeedbackResponse savedResponse = feedbackResponseRepository.save(feedbackResponse);

        feedback.setFeedbackResponse(savedResponse);
        feedbackRepository.save(feedback);

        return savedResponse;
    }
}
