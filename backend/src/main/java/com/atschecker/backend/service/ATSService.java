package com.atschecker.backend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.atschecker.backend.model.ScoreResult;

@Service
public class ATSService {

    private final EmbeddingService embeddingService;

    public ATSService(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    public ScoreResult analyze(String jd, String resume) {

        List<String> jdPhrases = extractPhrases(jd);
        List<String> resumePhrases = extractPhrases(resume);

        List<List<Double>> jdVec = embeddingService.getEmbeddings(jdPhrases);
        List<List<Double>> resVec = embeddingService.getEmbeddings(resumePhrases);

        if (jdVec.isEmpty() || resVec.isEmpty()) {
            return new ScoreResult(0, List.of(), List.of());
        }

        Set<String> matched = new HashSet<>();
        Set<String> missing = new HashSet<>();

        for (int i = 0; i < jdPhrases.size(); i++) {

            double best = 0;

            for (int j = 0; j < resumePhrases.size(); j++) {
                double sim = cosine(jdVec.get(i), resVec.get(j));
                best = Math.max(best, sim);
            }

            if (best > 0.78) {
                matched.add(jdPhrases.get(i));
            } else {
                missing.add(jdPhrases.get(i));
            }
        }

        int score = jdPhrases.isEmpty()
                ? 0
                : (int) ((double) matched.size() / jdPhrases.size() * 100);

        return new ScoreResult(
                score,
                new ArrayList<>(matched),
                new ArrayList<>(missing)
        );
    }

    // -CLEAN PHRASE ----
    private List<String> extractPhrases(String text) {

        if (text == null || text.isBlank()) return List.of();

        text = text.toLowerCase()
                .replaceAll("[^a-z0-9+.#/\\-\\s]", " ");

        String[] words = text.split("\\s+");

        List<String> phrases = new ArrayList<>();

       
        for (String w : words) {
            if (isTechnicalToken(w)) {
                phrases.add(w);
            }
        }

        for (int i = 0; i < words.length - 1; i++) {

            String w1 = words[i];
            String w2 = words[i + 1];

            if (isTechnicalToken(w1) && isTechnicalToken(w2)) {
                String phrase = w1 + " " + w2;
                phrases.add(phrase);
            }
        }

        return phrases.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    
    private boolean isTechnicalToken(String w) {

        if (w == null || w.length() < 3) return false;

        return w.contains("java") ||
                w.contains("spring") ||
                w.contains("node") ||
                w.contains("react") ||
                w.contains("angular") ||
                w.contains("aws") ||
                w.contains("azure") ||
                w.contains("docker") ||
                w.contains("kubernetes") ||
                w.contains("sql") ||
                w.contains("api") ||
                w.contains("ci") ||
                w.contains("cd") ||
                w.contains("github") ||
                w.contains("microservice") ||
                w.contains("rest") ||
                w.contains("cloud") ||
                w.contains("testing") ||
                w.contains("jenkins") ||
                w.contains("devops");
    }

  
    private double cosine(List<Double> a, List<Double> b) {

        double dot = 0, magA = 0, magB = 0;

        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            magA += a.get(i) * a.get(i);
            magB += b.get(i) * b.get(i);
        }

        return dot / (Math.sqrt(magA) * Math.sqrt(magB));
    }
}