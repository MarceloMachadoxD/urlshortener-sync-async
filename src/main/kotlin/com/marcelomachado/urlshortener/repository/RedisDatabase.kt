package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import reactor.core.publisher.Mono

interface RedisDatabase {

    fun saveUrl(url: UrlShort): Mono<UrlShort>

    fun getUrl(url: String): Mono<String>
}