package com.marcelomachado.urlshortener.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class urlShortenerControllerTests {

    private int BAD_REQUEST = 400;
    private int CREATED = 201;


    @Autowired
    private UrlShortenerController urlShortenerController;

    @Test
    @DisplayName("When receive a valid url should return a response with CREATED status")
    void postUrl_shouldReturnUrl() {
        String url = "https://www.marcelomachado.com";

        ResponseEntity<String> response = urlShortenerController.postUrl(url);

        Assertions.assertEquals(CREATED, response.getStatusCodeValue());
        Assertions.assertEquals(url, response.getBody());
    }

    @Test
    @DisplayName("When receive an incomplete url should return a response with BADREQUEST status")
    void postUrl_shouldReturnBadRequest_incompleteURL() {
        String url = "marcelomachado.com";

        ResponseEntity<String> response = urlShortenerController.postUrl(url);

        Assertions.assertEquals(BAD_REQUEST, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("When receive an invalid url should return a response with BADREQUEST status")
    void postUrl_shouldReturnBadRequest_invalidURL() {
        String url = "marcelomachado";

        ResponseEntity<String> response = urlShortenerController.postUrl(url);

        Assertions.assertEquals(BAD_REQUEST, response.getStatusCodeValue());
    }

}