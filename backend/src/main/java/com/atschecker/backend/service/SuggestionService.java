package com.atschecker.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SuggestionService {

    public List<String> generate(List<String> missingKeywords, String jdText) {

        List<String> suggestions = new ArrayList<>();

        if (missingKeywords == null || missingKeywords.isEmpty()) {
            suggestions.add("Good match! Your resume already covers most required skills.");
            return suggestions;
        }

        for (String keyword : missingKeywords) {

            String cleanKeyword = keyword.trim();

            if (cleanKeyword.isEmpty()) continue;

            // Generic dynamic suggestion (NO hardcoding skill list)
            suggestions.add(buildSuggestion(cleanKeyword, jdText));
        }

        return suggestions;
    }

    private String buildSuggestion(String keyword, String jdText) {

        // Basic dynamic template (no fixed skill mapping)
        String base = "Add experience with " + keyword;

        // Try to make it slightly more contextual if JD contains it
        if (jdText != null && jdText.toLowerCase().contains(keyword.toLowerCase())) {
            return base + " as it is mentioned in the job description.";
        }

        return base + " in your projects or work experience.";
    }
}