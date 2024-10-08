package com.marcelomachado.urlshortener.controller

import com.marcelomachado.urlshortener.service.UrlShortenerService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/url-shortener")
class UrlShortenerController(private val urlShortenerService: UrlShortenerService) {

    private val logger = LoggerFactory.getLogger(UrlShortenerController::class.java)

    @GetMapping
    fun getUrl(@RequestParam url: String): Mono<ResponseEntity<String>> {
        return urlShortenerService.getOriginalUrl(url)
            .flatMap { originalUrl ->
                Mono.just(ResponseEntity.ok(originalUrl))
            }
            .onErrorResume { returnBadRequest(url) }
    }

    @PostMapping
    fun postUrl(@RequestParam url: String): Mono<ResponseEntity<String>> {
        return if (isValidUrl(url)) {
            urlShortenerService.shortenUrl(url)
                .map { shortenedUrl -> ResponseEntity.status(HttpStatus.CREATED).body(shortenedUrl) }
                .onErrorResume { returnBadRequest(url) }
        } else {
            returnBadRequest(url)
        }
    }

    @PostMapping("/kafka")
    fun postUrlKafka(@RequestParam url: String): Mono<ResponseEntity<String>> {
        return urlShortenerService.shortenUrlKafka(url)
            .map {
                ResponseEntity.status(HttpStatus.ACCEPTED).body(it)
            }
    }

    private fun returnBadRequest(url: String): Mono<ResponseEntity<String>> {
        logger.error("Invalid url: {}", url)
        return Mono.just(ResponseEntity.badRequest().build())
    }

    private fun isValidUrl(url: String): Boolean {
        val regex =
            "^(http|https):\\/\\/(?:[-\\w.]|(?:%[0-9a-fA-F][0-9a-fA-F]))+([\\w.,@?^=%&:/~+#]*[\\w@?^=%&:/~+#])?\$".toRegex()
        return url.isNotEmpty() && (url.startsWith("http://") || url.startsWith("https://")) && url.matches(regex)
    }
}
