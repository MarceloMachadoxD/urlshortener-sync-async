package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import reactor.core.publisher.Mono

interface Database {

    fun saveUrl(url: String): Mono<UrlShort>

    fun getUrl(url: String): Mono<String>
}