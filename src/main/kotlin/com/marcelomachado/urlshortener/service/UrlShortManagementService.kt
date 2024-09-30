package com.marcelomachado.urlshortener.service

import com.marcelomachado.urlshortener.entity.UrlShort
import com.marcelomachado.urlshortener.repository.RedisDatabase
import com.marcelomachado.urlshortener.repository.UrlShortRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlShortManagementService {

    @Autowired
    private lateinit var redisDatabase: RedisDatabase

    @Autowired
    private lateinit var urlShortRepository: UrlShortRepository

    private val logger = LoggerFactory.getLogger(this.javaClass)


    fun processUrl(urlShort: UrlShort): Mono<UrlShort> {

        return urlShortRepository.save(urlShort)
            .doOnSuccess { logger.info("Saved URL on Database: ${urlShort.originalUrl} as ${urlShort.hashedUrl}") }
            .flatMap { savedUrlShort ->
                redisDatabase.saveUrl(urlShort)
                    .doOnSuccess { logger.info("Saved URL on Redis: $urlShort.originalUrl as ${urlShort.hashedUrl}") }
            }
            .doOnError { error ->
                println("Error on save URL: ${error.message}")
            }
    }


}