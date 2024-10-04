package com.marcelomachado.urlshortener.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "url_short")
data class UrlShort(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("original_url")
    val originalUrl: String,

    @Column("hashed_url")
    val hashedUrl: String
)