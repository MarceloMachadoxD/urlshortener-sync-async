package com.marcelomachado.urlshortener.controller

import com.marcelomachado.urlshortener.service.UrlShortenerService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

@SpringBootTest
class UrlShortenerControllerTest {

    @Autowired
    private lateinit var urlShortenerController: UrlShortenerController

    @MockBean
    private lateinit var urlShortenerService: UrlShortenerService

    final val validUrl: String = "https://marcelomachado.com"
    final val invalidUrl: String = "invalid-url"
    final val shortenMockUrl: String = "url-shortened"
    final val badRequestStatusCode: Int = 400
    final val createdStatusCode: Int = 201

    @BeforeEach
    fun setup() {
        urlShortenerController = UrlShortenerController(urlShortenerService)
        Mockito.`when`(urlShortenerService.shortenUrl(validUrl)).thenReturn(Mono.just(shortenMockUrl))
    }

    @Test
    @DisplayName("Should return 400 when invalid url")
    fun postUrl_ShouldReturnBadRequest_WhenInvalidUrl() {
        val response = urlShortenerController.postUrl(invalidUrl)

        assertEquals(badRequestStatusCode, response.block()?.statusCode?.value())
    }

    @Test
    @DisplayName("Should return 201 when valid url")
    fun postUrl_ShouldReturnOk_WhenValidUrl() {

        val response = urlShortenerController.postUrl(validUrl).block()

        assertEquals(createdStatusCode, response?.statusCode?.value())
        assertEquals(shortenMockUrl, response?.body)
        verify(urlShortenerService, times(1)).shortenUrl(validUrl)
    }

}