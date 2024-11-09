package com.example.alumind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @OneToOne
    @JoinColumn(name = "feedback_response_id", referencedColumnName = "id")
    private FeedbackResponse feedbackResponse;

    public Feedback() {}

    public Feedback(String feedback, FeedbackResponse feedbackResponse) {
        this.feedback = feedback;
        this.feedbackResponse = feedbackResponse;
    }

    public Long getId() {
        return id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public FeedbackResponse getFeedbackResponse() {
        return feedbackResponse;
    }

    public void setFeedbackResponse(FeedbackResponse feedbackResponse) {
        this.feedbackResponse = feedbackResponse;
    }
}
