package com.marcelomachado.urlshortener.service

import com.marcelomachado.urlshortener.repository.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlShortenerService {

    @Autowired
    private lateinit var database: Database


    fun shortenUrl(url: String): Mono<String> {
        return database.saveUrl(url)
            .map { it?.hashedUrl ?: "not found" }
    }
}