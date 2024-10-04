package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import reactor.core.publisher.Mono

interface RedisDatabase {

    /**
     * Save a URL on Redis using the hashed URL as key and the original URL as value
     * it also configures the TTL of the key as defaultTtl setup in application.properties
     *
     * @param url URL to be saved
     *
     * @return Mono<UrlShort> with the saved hashed URL
     */
    fun saveUrl(url: UrlShort): Mono<UrlShort>

    /**
     * Get the original URL from Redis using the hashed URL
     *
     * @param hashed hashed URL
     *
     * @return Mono<String> with the original URL
     */
    fun getUrl(hashed: String): Mono<String>
}