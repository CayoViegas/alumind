package com.example.alumind.controller;

import com.example.alumind.dto.FeedbackRequest;
import com.example.alumind.model.FeedbackResponse;
import com.example.alumind.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> receiveFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        FeedbackResponse response = feedbackService.processFeedback(feedbackRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
