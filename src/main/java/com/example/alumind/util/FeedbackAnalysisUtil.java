package com.example.alumind.util;

import com.example.alumind.model.RequestedFeature;

import java.util.ArrayList;
import java.util.List;

public class FeedbackAnalysisUtil {

    public static String analyzeSentiment(String feedback) {
        if (feedback.toLowerCase().contains("não gostei")) {
            return "NEGATIVO";
        } else if (feedback.toLowerCase().contains("gostei")) {
            return "POSITIVO";
        }

        return "INCONCLUSIVO";
    }

    public static List<RequestedFeature> extractRequestedFeatures(String feedback) {
        List<RequestedFeature> features = new ArrayList<>();

        if (feedback.toLowerCase().contains("editar perfil")) {
            features.add(new RequestedFeature("EDITAR_PERFIL", "O usuário gostaria de realizar a edição do próprio perfil"));
        }

        if (feedback.toLowerCase().contains("gerenciar minha mesa")) {
            features.add(new RequestedFeature("GERENCIAR_MESA", "O usuário deseja funcionalidades para gerenciar sua mesa de RPG"));
        }

        return features;
    }
}
