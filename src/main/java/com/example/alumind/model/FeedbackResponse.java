package com.example.alumind.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "feedback_responses")
public class FeedbackResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sentiment;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "feedback_response_id")
    private List<RequestedFeature> requestedFeatures;

    @Column(columnDefinition = "TEXT")
    private String responseMessage;

    public FeedbackResponse() {}

    public FeedbackResponse(String sentiment, List<RequestedFeature> requestedFeatures) {
        this.sentiment = sentiment;
        this.requestedFeatures = requestedFeatures;
    }

    public FeedbackResponse(String sentiment, List<RequestedFeature> requestedFeatures, String responseMessage) {
        this.sentiment = sentiment;
        this.requestedFeatures = requestedFeatures;
        this.responseMessage = responseMessage;
    }

    public Long getId() {
        return id;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public List<RequestedFeature> getRequestedFeatures() {
        return requestedFeatures;
    }

    public void setRequestedFeatures(List<RequestedFeature> requestedFeatures) {
        this.requestedFeatures = requestedFeatures;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
