package com.example.shorturl.demo.service;

import org.springframework.stereotype.Service;

@Service
public class HashService {
   private static final int RADIX = 36;
    private static final String PIPE = "-";

    public String shorten(String url) {
        return encode(url);
    }
    private String encode(String url) {

        String hexValue = Integer.toString(url.hashCode(), RADIX);
        if (hexValue.startsWith(PIPE)) {
            hexValue = hexValue.substring(1);
        }

        return hexValue;
    }
}
