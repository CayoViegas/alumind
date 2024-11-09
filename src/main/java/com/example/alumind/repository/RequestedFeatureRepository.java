package com.example.alumind.repository;

import com.example.alumind.model.RequestedFeature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestedFeatureRepository extends JpaRepository<RequestedFeature, Long> {
}
