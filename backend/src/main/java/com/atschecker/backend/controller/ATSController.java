package com.atschecker.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atschecker.backend.model.ScoreResult;
import com.atschecker.backend.service.ATSService;

@RestController
@RequestMapping("/api/ats")
@CrossOrigin("*")
public class ATSController {

    private final ATSService atsService;

    public ATSController(ATSService atsService) {
        this.atsService = atsService;
    }

    @PostMapping("/analyze")
    public ScoreResult analyze(@RequestBody Map<String, String> request) {

        String jd = request.get("jd");
        String resume = request.get("resume");

        if (jd == null || resume == null) {
            return new ScoreResult(0, List.of(), List.of());
        }

        return atsService.analyze(jd, resume);
    }
}