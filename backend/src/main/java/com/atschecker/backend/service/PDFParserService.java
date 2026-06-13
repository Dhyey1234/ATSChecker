package com.atschecker.backend.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class PDFParserService {

    public String extractText(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();

        if (filename == null) {
            throw new IOException("File has no name");
        }

        // Handle PDF files
        if (filename.endsWith(".pdf")) {
            PDDocument doc = Loader.loadPDF(file.getBytes());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            doc.close();
            return text;
        }

        // Handle plain text files
        if (filename.endsWith(".txt")) {
            return new String(file.getBytes());
        }

        throw new IOException("Unsupported file type. Use PDF or TXT.");
    }
}