package com.atschecker.backend.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SuggestionService {

    public List<String> generate(List<String> missing, String jd) {

        List<String> out = new ArrayList<>();

        for (String m : missing) {
            out.add("Add experience with " + m + " in your projects");
        }

        return out;
    }
}