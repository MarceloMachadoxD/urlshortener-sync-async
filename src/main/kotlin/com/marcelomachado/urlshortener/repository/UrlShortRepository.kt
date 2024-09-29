package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UrlShortRepository : ReactiveCrudRepository<UrlShort, Long> {

    fun findByOriginalUrl(originalUrl: String): Mono<UrlShort>

    fun findByHashedUrl(hashedUrl: String): Mono<UrlShort>

}