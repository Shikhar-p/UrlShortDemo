package com.urlshort.controller;

import com.urlshort.entity.UrlShortDemo;
import com.urlshort.service.UrlShortDemoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlShortDemoController {

    @Autowired
    private UrlShortDemoService urlShortDemoService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlShortDemo> shortenUrl(@RequestBody String originalUrl) {
        UrlShortDemo shortUrl = urlShortDemoService.shortenUrl(originalUrl);
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = urlShortDemoService.getOriginalUrl(shortUrl);
        response.sendRedirect(originalUrl);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/top-domains")
    public ResponseEntity<Map<String, Long>> getTopDomains() {
        return ResponseEntity.ok(urlShortDemoService.getTopDomains());
    }

}
