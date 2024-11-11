package com.example.alumind.controller;

import com.example.alumind.service.GroqService;
import com.example.alumind.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GroqService groqService;
    private String systemPrompt = Utils.loadSystemPrompt();

    @Autowired
    public ChatController(GroqService groqService) {
        this.groqService = groqService;
    }

    @PostMapping
    public GroqService.FeedbackFormat chat(@RequestParam String content) {
        return groqService.getChatResponse("llama-3.1-70b-versatile", content, systemPrompt, Double.valueOf("${temperature}"));
    }
}
