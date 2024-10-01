package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UrlShortRepository : ReactiveCrudRepository<UrlShort, Long> {

    fun findFirstByOriginalUrlOrderByIdDesc(originalUrl: String): Mono<UrlShort>

    fun findFirstByHashedUrlOrderByIdDesc(hashedUrl: String): Mono<UrlShort>

}