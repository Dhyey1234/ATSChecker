package com.atschecker.backend.model;

import java.util.List;

public class ATSResponse {

    private int score;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private List<String> suggestions;

    public ATSResponse(int score, List<String> matchedKeywords,
                       List<String> missingKeywords, List<String> suggestions) {
        this.score = score;
        this.matchedKeywords = matchedKeywords;
        this.missingKeywords = missingKeywords;
        this.suggestions = suggestions;
    }

    public int getScore() { return score; }
    public List<String> getMatchedKeywords() { return matchedKeywords; }
    public List<String> getMissingKeywords() { return missingKeywords; }
    public List<String> getSuggestions() { return suggestions; }
}