package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
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

    override fun getUrl(hashedUrl: String): Mono<String> {
        return redisTemplate.opsForValue().get(hashedUrl).flatMap { recoveredValue ->
            redisTemplate.expire(hashedUrl, defaultTtl).subscribe()
            Mono.just(recoveredValue)
        }
    }
}