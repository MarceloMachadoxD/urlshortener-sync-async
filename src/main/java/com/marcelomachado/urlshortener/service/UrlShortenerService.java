package com.marcelomachado.urlshortener.service;

import com.marcelomachado.urlshortener.entity.UrlShort;
import com.marcelomachado.urlshortener.repository.Database;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlShortenerService {

    private final Database database;

    @Value("${url-prefix}")
    private String urlPrefix;

    public String shortenUrl(String url) {
        String savedUrl = getUrl(url);
        if (null != savedUrl) {
            return urlPrefix.concat(savedUrl);
        }
        return urlPrefix.concat(saveUrl(url).getHashedUrl());
    }

    private String getUrl(String url) {
        UrlShort savedUrl = database.findUrl(url);
        if (null != savedUrl) {
            return savedUrl.getHashedUrl();
        }
        return null;
    }

    private UrlShort saveUrl(String url) {
        return database.saveUrl(url);
    }
}
