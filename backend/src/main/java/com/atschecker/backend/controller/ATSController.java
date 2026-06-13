package com.atschecker.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.atschecker.backend.model.ATSResult;
import com.atschecker.backend.service.KeywordFilterService;
import com.atschecker.backend.service.PDFParserService;
import com.atschecker.backend.service.TFIDFService;

@RestController
@RequestMapping("/api")
public class ATSController {

    private final PDFParserService pdfParser;
    private final TFIDFService tfidf;
    private final KeywordFilterService keywordFilter;

    public ATSController(PDFParserService pdfParser,
                         TFIDFService tfidf,
                         KeywordFilterService keywordFilter) {
        this.pdfParser = pdfParser;
        this.tfidf = tfidf;
        this.keywordFilter = keywordFilter;
    }

    // Both files
    @PostMapping("/analyze")
    public ResponseEntity<ATSResult> analyze(
            @RequestParam("jd") MultipartFile jdFile,
            @RequestParam("resume") MultipartFile resumeFile) {

        try {
            String jdText = pdfParser.extractText(jdFile);
            String resumeText = pdfParser.extractText(resumeFile);

            Map<String, Object> analysis = tfidf.analyze(jdText, resumeText);

            ATSResult result = new ATSResult(
                (int) analysis.get("score"),
                (int) analysis.get("totalJdKeywords"),
                (List<String>) analysis.get("matchedKeywords"),
                (List<String>) analysis.get("missingKeywords")
            );

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Both paste text
    @PostMapping("/analyze-text")
    public ResponseEntity<ATSResult> analyzeText(@RequestBody Map<String, String> body) {
        try {
            String jdText = body.get("jdText");
            String resumeText = body.get("resumeText");

            if (jdText == null || resumeText == null) {
                return ResponseEntity.badRequest().build();
            }

            Map<String, Object> analysis = tfidf.analyze(jdText, resumeText);

            ATSResult result = new ATSResult(
                (int) analysis.get("score"),
                (int) analysis.get("totalJdKeywords"),
                (List<String>) analysis.get("matchedKeywords"),
                (List<String>) analysis.get("missingKeywords")
            );

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Mixed mode — any combination of file and paste
    @PostMapping("/analyze-mixed")
    public ResponseEntity<ATSResult> analyzeMixed(
            @RequestParam(value = "jdFile", required = false) MultipartFile jdFile,
            @RequestParam(value = "jdText", required = false) String jdText,
            @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestParam(value = "resumeText", required = false) String resumeText) {

        try {
            // Resolve JD
            String resolvedJd;
            if (jdFile != null && !jdFile.isEmpty()) {
                resolvedJd = pdfParser.extractText(jdFile);
            } else if (jdText != null && !jdText.isBlank()) {
                resolvedJd = jdText;
            } else {
                return ResponseEntity.badRequest().build();
            }

            // Resolve Resume
            String resolvedResume;
            if (resumeFile != null && !resumeFile.isEmpty()) {
                resolvedResume = pdfParser.extractText(resumeFile);
            } else if (resumeText != null && !resumeText.isBlank()) {
                resolvedResume = resumeText;
            } else {
                return ResponseEntity.badRequest().build();
            }

            Map<String, Object> analysis = tfidf.analyze(resolvedJd, resolvedResume);

            ATSResult result = new ATSResult(
                (int) analysis.get("score"),
                (int) analysis.get("totalJdKeywords"),
                (List<String>) analysis.get("matchedKeywords"),
                (List<String>) analysis.get("missingKeywords")
            );

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Debug — see all loaded stopwords
    @GetMapping("/stopwords")
    public ResponseEntity<Set<String>> getStopwords() {
        return ResponseEntity.ok(keywordFilter.getAllStopwords());
    }
}