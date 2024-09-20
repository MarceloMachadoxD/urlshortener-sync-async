package com.marcelomachado.urlshortener.repository;

import com.marcelomachado.urlshortener.entity.UrlShort;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


@Component
public class SampleMemoryDataBase implements Database {

    private HashMap<String, String> urlShortMap = new HashMap<>();


    @Override
    public UrlShort saveUrl(String url) {
        urlShortMap.put(url, hashUrl(url));
        return UrlShort.builder().originalUrl(url).hashedUrl(urlShortMap.get(url)).build();
    }

    private String hashUrl(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(url.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public UrlShort findUrl(String url) {
        if (urlShortMap.containsKey(url)) {
            return UrlShort.builder().originalUrl(url).hashedUrl(urlShortMap.get(url)).build();
        }
        return null;
    }

}
