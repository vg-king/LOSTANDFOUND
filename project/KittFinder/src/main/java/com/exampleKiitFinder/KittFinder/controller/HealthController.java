package com.exampleKiitFinder.KittFinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"https://kiitfinderui-abbi.vercel.app", "https://lostandfound-1-p1l9.onrender.com", "http://localhost:*"})
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "KIIT Finder API"
        ));
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(Map.of(
                "message", "KIIT Finder API is running",
                "status", "OK"
        ));
    }

    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public ResponseEntity<?> handleOptionsRequests() {
        return ResponseEntity.ok().build();
    }
}