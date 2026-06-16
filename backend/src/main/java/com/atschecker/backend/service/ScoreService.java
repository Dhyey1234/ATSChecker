package com.atschecker.backend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.atschecker.backend.model.ScoreResult;

@Service
public class ScoreService {

    private final KeywordExtractorService keywordExtractorService;

    public ScoreService(KeywordExtractorService keywordExtractorService) {
        this.keywordExtractorService = keywordExtractorService;
    }

    public ScoreResult calculate(String jd, String resume) {

        List<String> jdKeywords =
                keywordExtractorService.extractKeywords(jd);

        Set<String> resumeKeywords =
                new HashSet<>(keywordExtractorService.extractKeywords(resume));

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String keyword : jdKeywords) {

            if (resumeKeywords.contains(keyword)) {
                matched.add(keyword);
            } else {
                missing.add(keyword);
            }
        }

        int score = jdKeywords.isEmpty()
                ? 0
                : (int) (((double) matched.size() / jdKeywords.size()) * 100);

        return new ScoreResult(
                score,
                matched,
                missing
        );
    }
}