package com.marcelomachado.urlshortener.controller;

import com.marcelomachado.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class urlShortenerControllerTests {

    private int BAD_REQUEST = 400;
    private int CREATED = 201;

    @Mock
    private UrlShortenerService urlShortenerService;

    @Autowired
    private UrlShortenerController urlShortenerController;

    @BeforeEach
    void setup() {
        urlShortenerController = new UrlShortenerController(urlShortenerService);
    }

    @Test
    @DisplayName("When receive a valid url should return a response with CREATED status")
    void postUrl_shouldReturnUrl() {
        String url = "https://www.marcelomachado.com";
        Mockito.when(urlShortenerService.shortenUrl(Mockito.any())).thenReturn("index/hash");

        ResponseEntity<String> response = urlShortenerController.postUrl(url);

        Assertions.assertEquals(CREATED, response.getStatusCode().value());
        Assertions.assertEquals("index/hash", response.getBody());
    }

    @Test
    @DisplayName("When receive an incomplete url should return a response with BADREQUEST status")
    void postUrl_shouldReturnBadRequest_incompleteURL() {
        String url = "marcelomachado.com";

        ResponseEntity<String> response = urlShortenerController.postUrl(url);

        Assertions.assertEquals(BAD_REQUEST, response.getStatusCode().value());
    }

    @Test
    @DisplayName("When receive an invalid url should return a response with BADREQUEST status")
    void postUrl_shouldReturnBadRequest_invalidURL() {
        String url = "marcelomachado";

        ResponseEntity<String> response = urlShortenerController.postUrl(url);

        Assertions.assertEquals(BAD_REQUEST, response.getStatusCode().value());
    }

}