package org.example.rowingcoaching.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String health() {
        return "RowingCoaching API is running!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
} 