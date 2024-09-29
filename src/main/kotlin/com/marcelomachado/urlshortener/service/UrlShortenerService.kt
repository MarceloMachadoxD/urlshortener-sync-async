package com.marcelomachado.urlshortener.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlShortenerService {

    fun shortenUrl(url: String): Mono<String> {
        return Mono.just("https://marcelomachado.com/$url")
    }
}