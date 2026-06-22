package com.atschecker.backend.model;

import java.util.List;
import java.util.Map;

public class ScoreResult {

    private int score;
    private List<String> matched;
    private List<String> missing;
    private Map<String, Integer> categoryScores;
    private List<String> suggestions;

    public ScoreResult(int score,
                       List<String> matched,
                       List<String> missing,
                       Map<String, Integer> categoryScores,
                       List<String> suggestions) {
        this.score = score;
        this.matched = matched;
        this.missing = missing;
        this.categoryScores = categoryScores;
        this.suggestions = suggestions;
    }

    public int getScore() { return score; }
    public List<String> getMatched() { return matched; }
    public List<String> getMissing() { return missing; }
    public Map<String, Integer> getCategoryScores() { return categoryScores; }
    public List<String> getSuggestions() { return suggestions; }
}