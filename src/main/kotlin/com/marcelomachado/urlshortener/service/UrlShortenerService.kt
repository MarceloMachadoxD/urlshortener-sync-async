package com.marcelomachado.urlshortener.service

import com.marcelomachado.urlshortener.entity.UrlShort
import com.marcelomachado.urlshortener.util.Utils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlShortenerService {

    @Autowired
    private lateinit var urlShortManagementService: UrlShortManagementService

    @Value("\${app.base-url}")
    private lateinit var baseUrl: String

    private val logger = LoggerFactory.getLogger(this.javaClass)


    fun shortenUrl(url: String): Mono<String> {
        val hashedUrl = Utils.md5(url)
        val urlShort = UrlShort(null, url, hashedUrl)

        return urlShortManagementService.processUrl(urlShort)
            .map { savedUrlShort ->
                logger.info("Saved URL in both databases: $savedUrlShort")
                baseUrl + savedUrlShort.hashedUrl
            }
            .doOnError { error -> logger.error("Error saving URL: ${error.message}") }
    }
}