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
        return urlShortRepository.findFirstByOriginalUrlOrderByIdDesc(urlShort.originalUrl)
            .flatMap { existingUrlShort ->
                logger.info("URL already on Database: ${urlShort.originalUrl} as ${urlShort.hashedUrl}")
                Mono.just(existingUrlShort)
            }
            .switchIfEmpty(
                urlShortRepository.save(urlShort)
                    .doOnSuccess { savedUrlShort ->
                        logger.info("Saved URL on Database: ${savedUrlShort.originalUrl} as ${savedUrlShort.hashedUrl}")
                    }
                    .doOnError { error ->
                        logger.error("Error saving URL to Database: ${urlShort.originalUrl}, Error: ${error.message}")
                    }
            )
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

    fun getOriginalUrl(hashedUrl: String): Mono<String> {
        return getOriginalUrlFromRedis(hashedUrl)
            .doOnSuccess { originalUrl ->
                if (originalUrl != null) {
                    logger.info("Original URL from Redis: $originalUrl")
                }
            }
            .switchIfEmpty(
                getOriginalUrlFromDatabase(hashedUrl)
                    .doOnSuccess { originalUrl ->
                        logger.info("Original URL from Database: $originalUrl")
                    }
            )
            .doOnError { error ->
                logger.error("Error getting original URL: ${hashedUrl}, Error: ${error.message}")
            }
    }

    private fun getOriginalUrlFromRedis(hashedUrl: String): Mono<String> {
        return redisDatabase.getUrl(hashedUrl)
    }

    private fun getOriginalUrlFromDatabase(hashedUrl: String): Mono<String> {
        return urlShortRepository.findFirstByHashedUrlOrderByIdDesc(hashedUrl)
            .map { it.originalUrl }
    }
}