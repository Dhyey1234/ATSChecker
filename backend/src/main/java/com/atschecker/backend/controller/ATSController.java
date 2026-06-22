package com.atschecker.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atschecker.backend.model.ATSRequest;
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
    public ResponseEntity<ScoreResult> analyze(@RequestBody ATSRequest request) {

        // Basic validation (prevents silent null crashes downstream)
        if (request == null ||
            request.getJobDescription() == null ||
            request.getResume() == null) {

            return ResponseEntity.badRequest()
                    .body(new ScoreResult(
                            0,
                            java.util.List.of(),
                            java.util.List.of(),
                            java.util.Map.of(),
                            java.util.List.of("Invalid input: Resume or Job Description is missing")
                    ));
        }

        ScoreResult result = atsService.analyze(
                request.getJobDescription(),
                request.getResume()
        );

        return ResponseEntity.ok(result);
    }
}