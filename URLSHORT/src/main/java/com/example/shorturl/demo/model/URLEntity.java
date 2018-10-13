package com.example.shorturl.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
//@EntityListeners({AuditingEntityListener.class})
@Table(name="URL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class URLEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "url", nullable = false, unique = true)
    private String url;
    @Column(name = "hash", nullable = false, unique = true)
    private String hash;

    public URLEntity(){}

    public URLEntity(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

