package com.marcelomachado.urlshortener.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlShort {

    private Long id;
    private String originalUrl;
    private String hashedUrl;
}
