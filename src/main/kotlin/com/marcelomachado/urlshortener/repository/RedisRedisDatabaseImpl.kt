package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import com.marcelomachado.urlshortener.util.Utils.md5
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class RedisRedisDatabaseImpl(private val redisTemplate: ReactiveRedisTemplate<String, String>) : RedisDatabase {

    private val defaultTtl: Duration = Duration.ofMinutes(2)
    override fun saveUrl(url: UrlShort): Mono<UrlShort> {
        return redisTemplate.opsForValue()
            .set(url.hashedUrl, url.originalUrl, defaultTtl)
            .thenReturn(UrlShort(null, url.originalUrl, url.hashedUrl))

    }

    override fun getUrl(url: String): Mono<String> {
        val hashedUrl = md5(url)
        redisTemplate.opsForValue().get(hashedUrl)
        return Mono.just(url)
    }
}