package com.atschecker.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.atschecker.backend.model.ScoreResult;

@Service
public class ScoreService {

    public ScoreResult calculate(String jd, String resume) {
        int score = 0;

        List<String> matched = List.of();
        List<String> missing = List.of();

        Map<String, Integer> categoryScores = Map.of();

        List<String> suggestions = List.of(
                "Use ATSService for full analysis instead of ScoreService"
        );

        return new ScoreResult(
                score,
                matched,
                missing,
                categoryScores,
                suggestions
        );
    }
}