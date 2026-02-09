package com.backend.movie_matrix.controllers;

import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// HealthController.java
@RestController
@RequestMapping("/api")
public class HealthController { 

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", new Date());
        response.put("service", "movie-matrix-backend");
        response.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
