package com.example.alumind.util;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static final Double TEMPERATURE = 0.6;

    public static String loadSystemPrompt() {
        try {
            Path path = ResourceUtils.getFile("classpath:prompts/SYSTEM_PROMPT.txt").toPath();
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o prompt do sistema.", e);
        }
    }
}
