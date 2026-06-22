package com.atschecker.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.atschecker.backend.model.ScoreResult;

@Service
public class ATSService {

    private final EmbeddingService embeddingService;
    private final SkillClassifierService classifier;

    public ATSService(EmbeddingService embeddingService,
                      SkillClassifierService classifier) {
        this.embeddingService = embeddingService;
        this.classifier = classifier;
    }

    public ScoreResult analyze(String jd, String resume) {

        List<String> jdSkills = extractSkills(jd);
        List<String> resumeSkills = extractSkills(resume);

        if (jdSkills.isEmpty()) {
            return new ScoreResult(0, List.of(), List.of(), Map.of(), List.of());
        }

        List<List<Double>> jdVec = embeddingService.getEmbeddings(jdSkills);
        List<List<Double>> resVec = embeddingService.getEmbeddings(resumeSkills);

        // 🔥 safety check (IMPORTANT)
        if (jdVec.isEmpty() || resVec.isEmpty()) {
            return new ScoreResult(
                    0,
                    List.of(),
                    jdSkills,
                    Map.of(),
                    List.of("Embedding service failed or returned empty vectors")
            );
        }

        Set<String> matched = new HashSet<>();
        Set<String> missing = new HashSet<>();

        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> categoryMatch = new HashMap<>();
        Map<String, Integer> categoryScores = new HashMap<>();

        for (int i = 0; i < jdSkills.size(); i++) {

            String skill = jdSkills.get(i);
            String category = classifier.classify(skill);

            categoryCount.put(category,
                    categoryCount.getOrDefault(category, 0) + 1);

            double best = 0;

            for (List<Double> rv : resVec) {
                best = Math.max(best, cosine(jdVec.get(i), rv));
            }

            if (best > 0.65) {
                matched.add(skill);
                categoryMatch.put(category,
                        categoryMatch.getOrDefault(category, 0) + 1);
            } else {
                missing.add(skill);
            }
        }

        for (String c : categoryCount.keySet()) {
            int score = (int) (
                    (categoryMatch.getOrDefault(c, 0) * 100.0)
                            / categoryCount.get(c)
            );
            categoryScores.put(c, score);
        }

        int finalScore = jdSkills.isEmpty()
                ? 0
                : (matched.size() * 100 / jdSkills.size());

        List<String> suggestions = missing.stream()
                .map(s -> "Add " + s + " to improve your profile")
                .collect(Collectors.toList());

        return new ScoreResult(
                finalScore,
                new ArrayList<>(matched),
                new ArrayList<>(missing),
                categoryScores,
                suggestions
        );
    }

    // 🔥 CLEAN skill extractor (NO garbage words)
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "and","or","for","the","a","an","to","of","in","on","with","as",
            "is","are","was","were","be","been","being","it","this","that",
            "by","from","at","within","using","apply","work","working",
            "experience","strong","good","ability","skills","knowledge",
            "projects","project","team","software","development","system",
            "systems","tools","environment","environments","issues","solutions"
    ));

    private List<String> extractSkills(String text) {

        if (text == null || text.isBlank()) return List.of();

        text = text.toLowerCase()
                .replace(",", " ")
                .replace(".", " ")
                .replaceAll("[^a-z0-9+#. ]", " ");

        return Arrays.stream(text.split("\\s+"))
                .map(String::trim)
                .filter(w -> w.length() > 2)
                .filter(w -> !STOPWORDS.contains(w))
                .distinct()
                .collect(Collectors.toList());
    }

    private double cosine(List<Double> a, List<Double> b) {

        double dot = 0, magA = 0, magB = 0;

        int len = Math.min(a.size(), b.size());

        for (int i = 0; i < len; i++) {
            dot += a.get(i) * b.get(i);
        }

        for (double v : a) magA += v * v;
        for (double v : b) magB += v * v;

        double denom = Math.sqrt(magA) * Math.sqrt(magB);
        return denom == 0 ? 0 : dot / denom;
    }
}