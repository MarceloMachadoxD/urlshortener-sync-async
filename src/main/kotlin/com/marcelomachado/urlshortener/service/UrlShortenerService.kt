package com.marcelomachado.urlshortener.service

import com.marcelomachado.urlshortener.repository.Database
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

    @Value("\${app.base-url}")
    private lateinit var baseUrl: String

    fun shortenUrl(url: String): Mono<String> {
        return database.saveUrl(url)
            .map { baseUrl.plus(it?.hashedUrl) }
    }
}