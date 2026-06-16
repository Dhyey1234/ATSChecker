package com.atschecker.backend.service;

import org.springframework.stereotype.Service;

@Service
public class TextPreprocessor {

    public String clean(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9 ]", " ");
    }
}