package com.atschecker.backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SkillClassifierService {

    private final Map<String, String> skillMap = new HashMap<>();

    public SkillClassifierService() {

        // Backend
        skillMap.put("java", "backend");
        skillMap.put("spring", "backend");
        skillMap.put("spring boot", "backend");
        skillMap.put("node", "backend");
        skillMap.put("api", "backend");
        skillMap.put("microservice", "backend");

        // Frontend
        skillMap.put("react", "frontend");
        skillMap.put("angular", "frontend");
        skillMap.put("html", "frontend");
        skillMap.put("css", "frontend");
        skillMap.put("javascript", "frontend");
        skillMap.put("js", "frontend");

        // DevOps
        skillMap.put("docker", "devops");
        skillMap.put("kubernetes", "devops");
        skillMap.put("ci", "devops");
        skillMap.put("cd", "devops");
        skillMap.put("github", "devops");
        skillMap.put("git", "devops");

        // Cloud
        skillMap.put("aws", "cloud");
        skillMap.put("azure", "cloud");
        skillMap.put("gcp", "cloud");

        // Database
        skillMap.put("sql", "database");
        skillMap.put("mysql", "database");
        skillMap.put("mongodb", "database");
        skillMap.put("postgres", "database");
    }

    public String classify(String skill) {

        if (skill == null) return "other";

        String s = skill.toLowerCase().trim();

        if (s.length() < 2) return "other";

        for (Map.Entry<String, String> entry : skillMap.entrySet()) {
            if (s.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "other";
    }
}