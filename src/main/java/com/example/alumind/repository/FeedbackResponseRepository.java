package com.example.alumind.repository;

import com.example.alumind.model.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long> {
}
