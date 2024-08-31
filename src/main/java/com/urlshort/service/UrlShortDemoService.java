package com.urlshort.service;

import com.urlshort.entity.UrlShortDemo;
import com.urlshort.repo.UrlShortDemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UrlShortDemoService {

    @Autowired
    private UrlShortDemoRepository urlShortDemoRepository;

    public UrlShortDemo shortenUrl(String originalUrl) {
        Optional<UrlShortDemo> existingUrl = urlShortDemoRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }
        String shortUrl = generateShortUrl();
        UrlShortDemo url = new UrlShortDemo();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);
        return urlShortDemoRepository.save(url);
    }

    public String getOriginalUrl(String shortUrl) {
        return urlShortDemoRepository.findByShortUrl(shortUrl)
                .map(UrlShortDemo::getOriginalUrl)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }

    private String generateShortUrl() {
        Random random = new Random();
        int randomInt = random.nextInt(1000); // integer from 0 to 999.
        return Integer.toString(randomInt, 36); //string in base-36. Base-36 uses digits 0-9 and letters a-z
    }

    public Map<String, Long> getTopDomains() {
        List<UrlShortDemo> urls = urlShortDemoRepository.findAll();
        Map<String, Long> domainCount = urls.stream()
                .collect(Collectors.groupingBy(url -> getDomainName(url.getOriginalUrl()), Collectors.counting()));

        return domainCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public String getDomainName(String originalUrl) {
        try {
            URI uri = new URI(originalUrl);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception e) {
            throw new RuntimeException("Invalid URL");
        }
    }
}
