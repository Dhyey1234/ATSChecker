package com.atschecker.backend.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class KeywordFilterService {

    private final Set<String> stopwords = new HashSet<>();

    private static final String STOPWORDS_URL =
        "https://raw.githubusercontent.com/stopwords-iso/stopwords-en/master/stopwords-en.txt";

    @PostConstruct
    public void loadStopwords() {
        // First try fetching from the URL
        boolean fetched = fetchFromURL();

        // If URL fails fall back to local stopwords.txt
        if (!fetched) {
            System.out.println("⚠️  Falling back to local stopwords.txt");
            loadFromLocal();
        }

        // Always add our custom tech/job specific garbage words on top
        addCustomStopwords();

        System.out.println("✅ Loaded " + stopwords.size() + " stopwords");
    }

    private boolean fetchFromURL() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STOPWORDS_URL))
                .GET()
                .build();

            HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                String[] lines = response.body().split("\\r?\\n");
                for (String line : lines) {
                    String word = line.trim().toLowerCase();
                    if (!word.isEmpty()) {
                        stopwords.add(word);
                    }
                }
                System.out.println("🌐 Fetched stopwords from GitHub");
                return true;
            }

        } catch (Exception e) {
            System.err.println("❌ Could not fetch stopwords from URL: " + e.getMessage());
        }
        return false;
    }

    private void loadFromLocal() {
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("stopwords.txt");
             BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) stopwords.add(word);
            }

        } catch (Exception e) {
            System.err.println("❌ Could not load local stopwords.txt: " + e.getMessage());
        }
    }

    // Your own custom words on top of the downloaded list
    private void addCustomStopwords() {
        stopwords.addAll(Arrays.asList(
            // numbers / symbols
            "000", "yrs", "etc", "eg", "ie",
            // job description filler
            "role", "job", "position", "company", "team", "work",
            "working", "looking", "seeking", "required", "requirements",
            "preferred", "bonus", "plus", "strong", "good", "great",
            "excellent", "ability", "able", "knowledge", "understanding",
            "experience", "experienced", "background", "familiarity",
            "exposure", "skills", "skill", "proficiency", "proficient",
            "demonstrated", "proven", "solid", "deep", "hands",
            // generic business
            "global", "leading", "leader", "world", "best", "top",
            "fast", "growing", "innovative", "dynamic", "passionate",
            "driven", "motivated", "collaborative", "cross", "functional",
            "stakeholder", "stakeholders", "ownership", "impact",
            "deliver", "delivering", "delivery", "drive", "driving",
            // common resume filler
            "responsible", "responsibilities", "worked", "managed",
            "helped", "assisted", "support", "supporting", "ensure",
            "ensuring", "maintain", "maintaining", "provide", "providing"
        ));
    }

    public boolean isGarbage(String word) {
        if (word == null || word.isBlank()) return true;
        if (word.length() <= 2) return true;
        if (word.matches(".*\\d.*")) return true;
        return stopwords.contains(word.toLowerCase());
    }

    public Set<String> getAllStopwords() {
        return Collections.unmodifiableSet(stopwords);
    }

    public void addStopword(String word) {
        stopwords.add(word.toLowerCase().trim());
    }
}