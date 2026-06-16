package com.atschecker.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<List<Double>> getEmbeddings(List<String> texts) {

        try {
            String url = "http://localhost:8000/embed";

            Map<String, Object> request = new HashMap<>();
            request.put("texts", texts);

            Map response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || response.get("vectors") == null) {
                return List.of();
            }

            return (List<List<Double>>) response.get("vectors");

        } catch (Exception e) {
            return List.of();
        }
    }
}