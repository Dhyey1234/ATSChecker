package com.atschecker.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TFIDFService {

    private final TextPreprocessor preprocessor;
    private final TechDictionary techDictionary;

    private static final int MAX_MISSING = 15;
    private static final int MAX_MATCHED = 20;
    private static final double MIN_TF_THRESHOLD = 0.003;

    public TFIDFService(TextPreprocessor preprocessor, TechDictionary techDictionary) {
        this.preprocessor = preprocessor;
        this.techDictionary = techDictionary;
    }

    private Map<String, Double> computeTF(List<String> tokens) {
        Map<String, Integer> wordCount = preprocessor.getWordCount(tokens);
        Map<String, Double> tf = new HashMap<>();
        int total = tokens.size();
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            tf.put(entry.getKey(), (double) entry.getValue() / total);
        }
        return tf;
    }

    public double cosineSimilarity(Map<String, Double> vectorA,
                                   Map<String, Double> vectorB) {
        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        for (String word : vectorA.keySet()) {
            if (vectorB.containsKey(word)) {
                dotProduct += vectorA.get(word) * vectorB.get(word);
            }
            magnitudeA += Math.pow(vectorA.get(word), 2);
        }
        for (double val : vectorB.values()) {
            magnitudeB += Math.pow(val, 2);
        }

        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        if (magnitudeA == 0 || magnitudeB == 0) return 0.0;
        return dotProduct / (magnitudeA * magnitudeB);
    }

    public Map<String, Object> analyze(String jdText, String resumeText) {
        List<String> jdTokens = preprocessor.tokenize(jdText);
        List<String> resumeTokens = preprocessor.tokenize(resumeText);

        Map<String, Double> jdVector = computeTF(jdTokens);
        Map<String, Double> resumeVector = computeTF(resumeTokens);

        // Score uses ALL tokens — not just tech terms
        double similarity = cosineSimilarity(jdVector, resumeVector);
        int score = (int) Math.round(similarity * 100);

        Set<String> resumeKeywords = new HashSet<>(resumeTokens);

        // Only rank JD words that are real tech terms
        List<String> rankedJdKeywords = jdVector.entrySet().stream()
                .filter(e -> e.getValue() >= MIN_TF_THRESHOLD)
                .filter(e -> techDictionary.isTechTerm(e.getKey()))
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String keyword : rankedJdKeywords) {
            if (resumeKeywords.contains(keyword)) {
                matched.add(keyword);
            } else {
                missing.add(keyword);
            }
        }

        List<String> topMatched = matched.stream()
                .limit(MAX_MATCHED)
                .sorted()
                .collect(Collectors.toList());

        List<String> topMissing = missing.stream()
                .limit(MAX_MISSING)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("totalJdKeywords", rankedJdKeywords.size());
        result.put("matchedKeywords", topMatched);
        result.put("missingKeywords", topMissing);

        return result;
    }
}