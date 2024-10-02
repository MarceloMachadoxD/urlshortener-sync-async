package com.marcelomachado.urlshortener.service

import reactor.core.publisher.Mono

interface UrlShortenerService {

    /**
     * Short the given URL and return the hashed URL with prefix setup in application.properties calling the proper
     * service to persist data
     *
     * @param url URL to be shortened
     *
     * @return Mono<String> with the shortened URL
     */
    fun shortenUrl(url: String): Mono<String>

    /**
     * Get the original URL from persisted database using the hashed URL as key
     *
     * @param hashedUrl hashed URL that can provide the prefix setup in application.properties or just hashCode
     *
     * @return Mono<String> with the original URL
     */
    fun getOriginalUrl(hashedUrl: String): Mono<String>
}