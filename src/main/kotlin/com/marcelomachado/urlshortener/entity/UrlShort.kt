package com.marcelomachado.urlshortener.entity

data class UrlShort(
    val id: Long,
    val originalUrl: String,
    val hashedUrl: String
)