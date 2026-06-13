package com.atschecker.backend.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TechDictionary {

    private static final Set<String> TECH_TERMS = new HashSet<>(Arrays.asList(

        // Languages
        "java", "python", "javascript", "typescript", "kotlin", "swift",
        "golang", "go", "rust", "scala", "ruby", "php", "csharp", "cpp",
        "c", "r", "matlab", "perl", "bash", "shell", "powershell",

        // Frontend
        "react", "angular", "vue", "nextjs", "nuxtjs", "svelte", "redux",
        "tailwind", "bootstrap", "sass", "scss", "webpack", "vite", "html",
        "css", "jsx", "tsx", "shadcn",

        // Backend
        "spring", "springboot", "django", "flask", "fastapi", "express",
        "nodejs", "nestjs", "laravel", "rails", "hibernate", "maven",
        "gradle", "tomcat", "jetty",

        // Cloud
        "aws", "gcp", "azure", "ec2", "s3", "lambda", "ecs", "eks",
        "cloudfront", "rds", "dynamodb", "sqs", "sns", "iam", "vpc",
        "vercel", "netlify", "heroku", "coolify", "digitalocean",

        // DevOps
        "docker", "kubernetes", "helm", "terraform", "ansible", "jenkins",
        "github", "gitlab", "bitbucket", "circleci", "argocd", "prometheus",
        "grafana", "nginx", "linux", "unix", "ci", "cd", "cicd",

        // Databases
        "postgresql", "mysql", "mongodb", "redis", "elasticsearch",
        "cassandra", "sqlite", "oracle", "mssql", "supabase", "firebase",
        "dynamodb", "neo4j", "influxdb", "mariadb",

        // AI / ML
        "openai", "langchain", "tensorflow", "pytorch", "keras", "sklearn",
        "huggingface", "llm", "gpt", "claude", "gemini", "rag", "vector",
        "embedding", "transformers", "mlflow", "pandas", "numpy", "scipy",
        "cursor", "codex", "copilot",

        // APIs / Protocols
        "rest", "graphql", "grpc", "soap", "websocket", "oauth", "jwt",
        "api", "json", "xml", "yaml", "protobuf",

        // Testing
        "junit", "jest", "pytest", "selenium", "cypress", "mockito",
        "testng", "postman", "insomnia",

        // Tools
        "git", "jira", "confluence", "figma", "notion", "slack",
        "vscode", "intellij", "eclipse", "vim",

        // Concepts (only ones that are genuinely technical)
        "microservices", "serverless", "monolith", "kafka", "rabbitmq",
        "elasticsearch", "etl", "orm", "mvc", "oop", "tdd", "ddd",
        "solid", "agile", "scrum", "devops", "mlops", "devsecops",

        // Mobile
        "android", "ios", "reactnative", "flutter", "swift", "kotlin",
        "xcode",

        // Data
        "spark", "hadoop", "airflow", "dbt", "snowflake", "bigquery",
        "tableau", "powerbi", "looker", "databricks",

        // Security
        "oauth", "saml", "ssl", "tls", "jwt", "encryption", "firewall",
        "penetration", "vulnerability", "siem", "iam",

        // Platforms
        "salesforce", "servicenow", "sap", "zendesk", "hubspot", "stripe",
        "twilio", "sendgrid"
    ));

    public boolean isTechTerm(String word) {
        if (word == null || word.isBlank()) return false;
        return TECH_TERMS.contains(word.toLowerCase().trim());
    }

    public Set<String> getAllTerms() {
        return Collections.unmodifiableSet(TECH_TERMS);
    }
}