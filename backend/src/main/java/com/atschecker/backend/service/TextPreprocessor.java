package com.atschecker.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TextPreprocessor {

    private final KeywordFilterService filter;

    public TextPreprocessor(KeywordFilterService filter) {
        this.filter = filter;
    }

    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();

        text = text.toLowerCase();
        text = text.replaceAll("[^a-z0-9\\s]", " ");
        String[] words = text.trim().split("\\s+");

        for (String word : words) {
            if (!filter.isGarbage(word)) {
                tokens.add(word);
            }
        }

        return tokens;
    }

    public Map<String, Integer> getWordCount(List<String> tokens) {
        Map<String, Integer> counts = new HashMap<>();
        for (String token : tokens) {
            counts.put(token, counts.getOrDefault(token, 0) + 1);
        }
        return counts;
    }
}