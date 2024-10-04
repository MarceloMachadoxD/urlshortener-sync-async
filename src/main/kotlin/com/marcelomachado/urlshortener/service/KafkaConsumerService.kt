package com.marcelomachado.urlshortener.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.marcelomachado.urlshortener.entity.UrlShort
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.empty

@Service
class KafkaConsumerService(
    @Value("\${kafka.bootstrap-servers}")
    private val bootstrapServers: String,

    @Value("\${kafka.topic}")
    private val topic: String,

    @Value("\${kafka.consumer.group-id}")
    private val groupId: String,

    private val urlShortManagementService: UrlShortManagementService,

    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private fun consumerFactory(): ConsumerFactory<String, String> {
        val configs = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.GROUP_ID_CONFIG to groupId
        )
        return DefaultKafkaConsumerFactory(configs)
    }

    @KafkaListener(topics = ["\${kafka.topic}"], groupId = "\${kafka.consumer.group-id}")
    fun consume(message: String) {
        logger.info("Message received: $message")
        processMessage(message)
            .subscribe(
                { logger.info("Message processed successfully") },
                { error -> logger.error("Error processing message: ${error.message}") }
            )
    }

    private fun processMessage(message: String): Mono<Void> {
        return Mono.fromCallable {
            val urlShort = deserializeMessage(message)
            processUrlShort(urlShort)
        }.flatMap { empty() }
    }

    private fun deserializeMessage(message: String): UrlShort {
        return try {
            objectMapper.readValue(message)
        } catch (e: Exception) {
            logger.error("Failed to deserialize message: $message, Error: ${e.message}")
            throw e
        }
    }

    private fun processUrlShort(urlShort: UrlShort) {
        logger.info("Try to save URL $urlShort.")

        urlShortManagementService.processUrl(urlShort)
            .doOnError { error ->
                logger.error("Error processing URL: ${urlShort.originalUrl}, Error: ${error.message}")
            }
            .doOnSuccess { shortenedUrl ->
                logger.info("Shortened from Kafka URL: $shortenedUrl")
            }
            .subscribe()
    }
}
