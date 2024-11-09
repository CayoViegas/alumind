package com.example.alumind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "requested_features")
public class RequestedFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String reason;

    public RequestedFeature() {}

    public RequestedFeature(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
