package com.atschecker.backend.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class KeywordExtractorService {

    public List<String> extractKeywords(String text) {

        if (text == null || text.isEmpty()) return List.of();

        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ");

        List<String> words = Arrays.stream(cleaned.split("\\s+"))
                .filter(w -> w.length() > 3)
                .filter(this::looksLikeUsefulToken)
                .collect(Collectors.toList());

        Map<String, Integer> scoreMap = new HashMap<>();

        // -------------------------
        // 1. SINGLE WORDS
        // -------------------------
        for (String w : words) {
            scoreMap.put(w, scoreMap.getOrDefault(w, 0) + 1);
        }

        // -------------------------
        // 2. BIGRAMS
        // -------------------------
        for (int i = 0; i < words.size() - 1; i++) {

            String w1 = words.get(i);
            String w2 = words.get(i + 1);

            if (!isValidPhrase(w1, w2)) continue;

            String bigram = w1 + " " + w2;

            scoreMap.put(bigram, scoreMap.getOrDefault(bigram, 0) + 3);
        }

        // -------------------------
        // 3. TRIGRAMS
        // -------------------------
        for (int i = 0; i < words.size() - 2; i++) {

            String w1 = words.get(i);
            String w2 = words.get(i + 1);
            String w3 = words.get(i + 2);

            if (!isValidPhrase(w1, w2, w3)) continue;

            String trigram = w1 + " " + w2 + " " + w3;

            scoreMap.put(trigram, scoreMap.getOrDefault(trigram, 0) + 5);
        }

        return scoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(40)
                .collect(Collectors.toList());
    }

    // -------------------------
    // REMOVE PURE NOISE TOKENS
    // -------------------------
    private boolean looksLikeUsefulToken(String word) {

        if (word == null || word.length() < 4) return false;

        if (Pattern.compile(".*\\d.*").matcher(word).matches()) return false;

        // removes obvious junk fragments
        return !(word.equals("this") ||
                word.equals("that") ||
                word.equals("with") ||
                word.equals("from") ||
                word.equals("into") ||
                word.equals("these") ||
                word.equals("those") ||
                word.equals("will") ||
                word.equals("have") ||
                word.equals("been"));
    }

    // -------------------------
    // VALID BIGRAM FILTER
    // prevents "best practices", "proven track", etc.
    // -------------------------
    private boolean isValidPhrase(String w1, String w2) {

        if (w1 == null || w2 == null) return false;

        // reject very generic patterns
        if (w1.equals(w2)) return false;

        // avoid abstract HR phrases
        if (isGenericWord(w1) || isGenericWord(w2)) return false;

        return true;
    }

    private boolean isValidPhrase(String w1, String w2, String w3) {

        if (!isValidPhrase(w1, w2)) return false;

        return !isGenericWord(w3);
    }

    private boolean isGenericWord(String w) {

        return w.equals("best") ||
                w.equals("practices") ||
                w.equals("experience") ||
                w.equals("track") ||
                w.equals("record") ||
                w.equals("proven") ||
                w.equals("delivery") ||
                w.equals("solution") ||
                w.equals("solutions") ||
                w.equals("customer") ||
                w.equals("customers") ||
                w.equals("performance");
    }
}