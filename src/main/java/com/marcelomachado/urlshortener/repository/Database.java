package com.marcelomachado.urlshortener.repository;

import com.marcelomachado.urlshortener.entity.UrlShort;

public interface Database {

    UrlShort saveUrl(String url);

    UrlShort findUrl(String url);
}
