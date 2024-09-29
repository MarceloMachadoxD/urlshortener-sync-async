package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import com.marcelomachado.urlshortener.util.Utils
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class SampleMemoryDataBase : Database {
    private val urlShortMap = LinkedHashMap<String?, String?>()

    override fun saveUrl(url: String): Mono<UrlShort> {
        urlShortMap.put(url, Utils.md5(url))
        val index = urlShortMap.keys.indexOf(url).toLong()

        val persistedHashedValue = urlShortMap.get(url).toString()

        val urlShort = UrlShort(index, url, persistedHashedValue)
        return Mono.just(urlShort)
    }

    override fun getUrl(url: String): Mono<String> {
        TODO("Not yet implemented")
    }

}
