# ATS Checker — Frontend

React + Vite frontend for the ATS Resume Checker tool.

## Setup

```bash
npm install
npm run dev
```

Runs on http://localhost:5173

## Backend

The frontend proxies `/api/*` to `http://localhost:8080` (your Spring Boot backend).

Make sure your Java backend is running before using the app.

## Expected API

**POST** `/api/analyze`

Form data:
- `jd` — Job description file (PDF/TXT/DOC)
- `resume` — Resume file (PDF/TXT/DOC)

Response JSON:
```json
{
  "score": 74,
  "totalJdKeywords": 20,
  "matchedKeywords": ["Java", "Spring Boot", "REST API", "Microservices"],
  "missingKeywords": ["Kubernetes", "Kafka", "Redis"]
}
```

## Project Structure

```
src/
├── App.jsx               # Root component + API call
├── App.css
├── index.css             # Global styles + design tokens
├── main.jsx              # React entry point
└── components/
    ├── UploadSection.jsx  # File upload with drag & drop
    ├── UploadSection.css
    ├── ResultSection.jsx  # Score ring + keyword pills
    └── ResultSection.css
```
