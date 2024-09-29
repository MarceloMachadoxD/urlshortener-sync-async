package com.marcelomachado.urlshortener.repository

import com.marcelomachado.urlshortener.entity.UrlShort
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Component
class SampleMemoryDataBase : Database {
    private val urlShortMap = LinkedHashMap<String?, String?>()

    override fun saveUrl(url: String): Mono<UrlShort> {
        urlShortMap.put(url, md5(url))
        val index = urlShortMap.keys.indexOf(url).toLong()

        val persistedHashedValue = urlShortMap.get(url).toString()

        val urlShort = UrlShort(index, url, persistedHashedValue)
        return Mono.just(urlShort)
    }

    override fun getUrl(url: String): Mono<String> {
        TODO("Not yet implemented")
    }

    private fun md5(input: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(input.toByteArray(StandardCharsets.UTF_8))
            val no = BigInteger(1, messageDigest)
            var hashText = no.toString(16)
            while (hashText.length < 32) {
                hashText = "0$hashText"
            }
            return hashText
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

}
