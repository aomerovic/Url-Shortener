package com.example.shorturl.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class URLModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    String url;
    Integer redirectType = 302;
    String shortUrl;
    int counter = 0;
    String accountId;

    public URLModel() {
    }

    public URLModel(String url, Integer redirectType, String shortUrl, int counter, String accountId) {
        this.url = url;
        this.redirectType = redirectType;
        this.shortUrl = shortUrl;
        this.counter = counter;
        this.accountId = accountId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
