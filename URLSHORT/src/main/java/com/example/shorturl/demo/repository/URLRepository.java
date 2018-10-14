package com.example.shorturl.demo.repository;

import com.example.shorturl.demo.model.URLModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLRepository extends CrudRepository<URLModel, Long> {
    URLModel findByAccountId(String accountId);
    URLModel findByShortUrl(String shortUrl);
    URLModel findByUrl(String url);


}
