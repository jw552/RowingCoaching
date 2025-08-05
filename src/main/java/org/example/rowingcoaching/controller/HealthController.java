package org.example.rowingcoaching.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@RestController
public class HealthController {

    @Autowired
    private Environment env;

    @GetMapping("/")
    public String health() {
        return "RowingCoaching API is running!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/debug")
    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DEBUG INFO ===\n");
        sb.append("Active Profiles: ").append(String.join(", ", env.getActiveProfiles())).append("\n");
        sb.append("Database URL: ").append(env.getProperty("spring.datasource.url")).append("\n");
        sb.append("Database User: ").append(env.getProperty("spring.datasource.username")).append("\n");
        sb.append("DATABASE_URL: ").append(env.getProperty("DATABASE_URL")).append("\n");
        sb.append("DATABASE_USERNAME: ").append(env.getProperty("DATABASE_USERNAME")).append("\n");
        sb.append("=== END DEBUG ===");
        return sb.toString();
    }

    @GetMapping("/test")
    public String test() {
        return "App is running without database!";
    }
} 