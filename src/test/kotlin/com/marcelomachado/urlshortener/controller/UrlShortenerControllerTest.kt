package com.marcelomachado.urlshortener.controller

import com.marcelomachado.urlshortener.service.UrlShortenerService
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

@SpringBootTest
class UrlShortenerControllerTest {

    @Autowired
    private lateinit var urlShortenerController: UrlShortenerController

    private lateinit var urlShortenerService: UrlShortenerService

    private val validUrl: String = "https://marcelomachado.com"
    private val invalidUrl: String = "invalid-url"
    private val shortenMockUrl: String = "url-shortened"
    private val badRequestStatusCode: Int = 400
    private val createdStatusCode: Int = 201

    @BeforeEach
    fun setup() {
        urlShortenerService = mockk()

        urlShortenerController = UrlShortenerController(urlShortenerService)

        every { urlShortenerService.shortenUrl(validUrl) } returns Mono.just(shortenMockUrl)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(urlShortenerService)
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

        verify(exactly = 1) { urlShortenerService.shortenUrl(validUrl) }
    }

}
