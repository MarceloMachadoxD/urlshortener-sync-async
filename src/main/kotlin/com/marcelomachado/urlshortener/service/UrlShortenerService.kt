package com.marcelomachado.urlshortener.service

import com.marcelomachado.urlshortener.entity.UrlShort
import com.marcelomachado.urlshortener.repository.Database
import com.marcelomachado.urlshortener.repository.UrlShortRepository
import com.marcelomachado.urlshortener.util.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlShortenerService {

    @Autowired
    @Qualifier("redisDatabase")
    private lateinit var database: Database

    @Autowired
    private lateinit var urlShortRepository: UrlShortRepository

    @Value("\${app.base-url}")
    private lateinit var baseUrl: String

    fun shortenUrl(url: String): Mono<String> {
        val hashedUrl = Utils.md5(url)
        val urlShort = UrlShort(null, url, hashedUrl)

        return urlShortRepository.save(urlShort)
            .doOnSuccess { println("Saved URL on Database: $urlShort") }
            .flatMap { savedUrlShort ->
                database.saveUrl(url)
                    .doOnSuccess { println("Saved URL on Redis: $url") }
                    .map { baseUrl + savedUrlShort.hashedUrl }
            }
            .doOnError { error ->
                println("Error on salve URL: ${error.message}")
            }
    }

}