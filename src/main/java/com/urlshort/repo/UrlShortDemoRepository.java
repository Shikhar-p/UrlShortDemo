package com.urlshort.repo;

import com.urlshort.entity.UrlShortDemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortDemoRepository extends JpaRepository<UrlShortDemo, Long> {

    Optional<UrlShortDemo> findByShortUrl(String shortUrl);
    Optional<UrlShortDemo> findByOriginalUrl(String originalUrl);

}
