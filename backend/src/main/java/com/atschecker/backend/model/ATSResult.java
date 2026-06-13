package com.atschecker.backend.model;

import java.util.List;

public class ATSResult {

    private int score;
    private int totalJdKeywords;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;

    // Constructor
    public ATSResult(int score, int totalJdKeywords,
                     List<String> matchedKeywords,
                     List<String> missingKeywords) {
        this.score = score;
        this.totalJdKeywords = totalJdKeywords;
        this.matchedKeywords = matchedKeywords;
        this.missingKeywords = missingKeywords;
    }

    // Getters (Spring needs these to convert to JSON)
    public int getScore() { return score; }
    public int getTotalJdKeywords() { return totalJdKeywords; }
    public List<String> getMatchedKeywords() { return matchedKeywords; }
    public List<String> getMissingKeywords() { return missingKeywords; }
}