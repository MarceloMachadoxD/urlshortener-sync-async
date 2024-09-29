package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import com.marcelomachado.urlshortener.util.Utils.md5
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class RedisDatabase(private val redisTemplate: ReactiveRedisTemplate<String, String>) : Database {

    private val defaultTtl: Duration = Duration.ofMinutes(2)
    override fun saveUrl(url: String): Mono<UrlShort> {
        val hashedUrl = md5(url)
        return redisTemplate.opsForValue()
            .set(hashedUrl, url, defaultTtl)
            .thenReturn(UrlShort(0, url, hashedUrl))

    }

    override fun getUrl(url: String): Mono<String> {
        redisTemplate.opsForValue().get(md5(url))
        return Mono.just(url)
    }
}