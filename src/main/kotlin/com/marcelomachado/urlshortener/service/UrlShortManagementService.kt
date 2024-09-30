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
        val saveToDbMono = saveToDatabase(urlShort)
        val saveToRedisMono = saveToRedis(urlShort)

        return Mono.zip(saveToDbMono, saveToRedisMono)
            .map { it.t1 }
            .doOnError { error ->
                logger.error("Error processing URL: ${urlShort.originalUrl}, Error: ${error.message}")
            }
    }

    private fun saveToDatabase(urlShort: UrlShort): Mono<UrlShort> {
        return urlShortRepository.save(urlShort)
            .doOnSuccess { savedUrlShort ->
                logger.info("Saved URL on Database: ${savedUrlShort.originalUrl} as ${savedUrlShort.hashedUrl}")
            }
            .doOnError { error ->
                logger.error("Error saving URL to Database: ${urlShort.originalUrl}, Error: ${error.message}")
            }
    }

    private fun saveToRedis(urlShort: UrlShort): Mono<UrlShort> {
        return redisDatabase.saveUrl(urlShort)
            .doOnSuccess {
                logger.info("Saved URL on Redis: ${urlShort.originalUrl} as ${urlShort.hashedUrl}")
            }
            .doOnError { error ->
                logger.error("Error saving URL to Redis: ${urlShort.originalUrl}, Error: ${error.message}")
            }
            .thenReturn(urlShort)
    }
}