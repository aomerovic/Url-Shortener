package com.example.shorturl.demo.helper;

import java.security.SecureRandom;

public class RandomString {

    public RandomString() {
    }
    private static final String symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom random = new SecureRandom();

    public static String generateRandomString(Integer len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(symbols.charAt(random.nextInt(symbols.length())));

        return sb.toString();
    }
}

