package com.marcelomachado.urlshortener.service

import com.marcelomachado.urlshortener.entity.UrlShort
import reactor.core.publisher.Mono

interface UrlShortManagementService {

    /**
     * With main responsibility is to persist the URL, save it in Database and Redis
     *
     * @param urlShort URL to be processed
     *
     * @return Mono<UrlShort> with the saved URL
     */
    fun processUrl(urlShort: UrlShort): Mono<UrlShort>

    /**
     * Get the original URL from persisted database using the hashed URL as key, it will first try to get it from Redis
     * and if it's not available, it will try to get it from Database
     *
     * @param hashedUrl hashed URL that can provide the prefix setup in application.properties or just hashCode
     *
     * @return Mono<String> with the original URL
     */
    fun getOriginalUrl(hashedUrl: String): Mono<String>

    /**
     * Send the shortened URL to Kafka topic to be processed by the consumer
     *
     *  @param urlShort URL to be sent to Kafka
     */
    fun processUrlKafka(urlShort: UrlShort): Mono<String>
}