package com.atschecker.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
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

            if (best > 0.6) matched.add(jdPhrases.get(i));
            else missing.add(jdPhrases.get(i));
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

    private List<String> extractPhrases(String text) {

        return Arrays.stream(text.toLowerCase()
                        .replaceAll("[^a-z0-9 ]", " ")
                        .split("\\s+"))
                .filter(w -> w.length() > 3)
                .collect(Collectors.toList());
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