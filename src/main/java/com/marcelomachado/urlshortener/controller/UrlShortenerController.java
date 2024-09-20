package com.marcelomachado.urlshortener.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/url-shortener")
@RestController
@Slf4j
public class UrlShortenerController {

    @GetMapping()
    public ResponseEntity<String> getUrl() {

        return ResponseEntity.ok("url");
    }

    @PostMapping()
    public ResponseEntity<String> postUrl() {
        return ResponseEntity.created(null).body("url");
    }
}
