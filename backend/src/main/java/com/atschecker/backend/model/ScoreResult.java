package com.atschecker.backend.model;

import java.util.List;

public class ScoreResult {

    private int score;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;

    public ScoreResult(int score, List<String> matchedKeywords, List<String> missingKeywords) {
        this.score = score;
        this.matchedKeywords = matchedKeywords;
        this.missingKeywords = missingKeywords;
    }

    public int getScore() {
        return score;
    }

    public List<String> getMatchedKeywords() {
        return matchedKeywords;
    }

    public List<String> getMissingKeywords() {
        return missingKeywords;
    }
}