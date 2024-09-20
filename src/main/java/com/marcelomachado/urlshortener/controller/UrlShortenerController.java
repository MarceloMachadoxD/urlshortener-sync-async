package com.marcelomachado.urlshortener.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/url-shortener")
@RestController
@Slf4j
public class UrlShortenerController {

    @GetMapping()
    public ResponseEntity<String> getUrl(@RequestParam String url) {

        return ResponseEntity.ok(url);
    }

    @PostMapping()
    public ResponseEntity<String> postUrl(@RequestParam String url) {
        if (!isValidUrl(url)) {
            log.error("Invalid url: {}", url);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }

    private boolean isValidUrl(String url) {
        if (null == url || url.isEmpty()) {
            return false;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }

        String regex = "^(http|https):\\/\\/(?:[-\\w.]|(?:%[0-9a-fA-F][0-9a-fA-F]))+([\\w.,@?^=%&amp;:/~+#]*[\\w@?^=%&amp;/~+#])?$";
        return url.matches(regex);
    }
}