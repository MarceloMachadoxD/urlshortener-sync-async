package com.marcelomachado.urlshortener.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.marcelomachado.urlshortener.entity.UrlShort
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class KafkaProducerService(
    @Value("\${kafka.bootstrap-servers}")
    private val bootstrapServers: String,
    @Value("\${kafka.topic}")
    private val topic: String
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val producerFactory = DefaultKafkaProducerFactory<String, String>(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )
    )

    private val kafkaTemplate = KafkaTemplate(producerFactory)

    fun sendMessage(message: UrlShort): Mono<String> {
        val jsonObject = ObjectMapper().writeValueAsString(message)
        return Mono.fromCallable {
            logger.info("Attempting to send message: $message to topic: $topic")
            kafkaTemplate.send(topic, jsonObject).get()  // Aguarda o envio completar
            logger.info("Message sent: $message")
            "Message sent successfully: $message"
        }
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume { error ->
                logger.error("Failed to send message: $message", error)
                Mono.just("Failed to send message: ${error.message}")
            }
    }
}

