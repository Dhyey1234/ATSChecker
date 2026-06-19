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

            Map<String, Object> req = new HashMap<>();
            req.put("texts", texts);

            Map res = restTemplate.postForObject(url, req, Map.class);

            if (res == null || res.get("vectors") == null) {
                return List.of();
            }

            return (List<List<Double>>) res.get("vectors");

        } catch (Exception e) {
            return List.of();
        }
    }
}