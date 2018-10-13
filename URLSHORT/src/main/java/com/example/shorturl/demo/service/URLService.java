package com.example.shorturl.demo.service;

import com.example.shorturl.demo.data.URLsSave;
import com.example.shorturl.demo.model.URLEntity;
import com.example.shorturl.demo.model.URLModel;
import com.example.shorturl.demo.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class URLService {
    @Autowired
    URLRepository urlRepository;

    protected List<URLModel> container = new ArrayList<>();

    public URLModel findByUrl(String url)
    {
       return urlRepository.findByUrl(url);
    }
    public URLModel findByShortUrl(String hash)
    {
        return urlRepository.findByShortUrl(hash);
    }

    public URLModel findByAccountId(String accountId)
    {
        return urlRepository.findByAccountId(accountId);
    }
    public void save(URLModel urlModel)
    {
        urlRepository.save(urlModel);
        container.add(urlModel);
    }
    public void incrementCounter(String url)
    {

        int counter = urlRepository.findByUrl(url).getCounter();
        System.out.println(counter);
        counter++;
        urlRepository.findByUrl(url).setCounter(counter);
        System.out.println(urlRepository.findByUrl(url).getCounter());
        URLModel model = urlRepository.findByUrl(url);
        System.out.println(model.getCounter());
        System.out.println(model);

        this.replace(model);
    }
    public void replace(URLModel model)
    {
        System.out.println(model.getCounter());
        for (URLModel urlMod: container
             ) {
            if(urlMod.getUrl().equals(model.getUrl()) && model.getAccountId() == urlMod.getAccountId()) {
                container.remove(urlMod);
                container.add(model);
                System.out.println(model.getCounter() + " " + model.getUrl());

            }

        }
    }

    public String findByModel(URLModel urlModel)
    {
        String url = "";
        for (URLModel urlMod: container
             ) {
            if(urlModel.equals(urlMod))
                url = urlModel.getUrl();
        }
        return url;
    }
    public URLModel findByURL(String url)
    {
        URLModel urlModel = null;
        for (URLModel urlMod: container
             ) {
            if(url.equals(urlMod.getUrl()))
                urlModel = urlMod;
        }
        return urlModel;

    }
    public URLModel findByShortURL(String url)
    {
        URLModel urlModel = null;
        for (URLModel urlMod: container
                ) {
            if(url.equals(urlMod.getShortUrl()))
                urlModel = urlMod;
        }
        return urlModel;

    }

    public boolean URLModelExists(URLModel urlModel)
    {
        for (URLModel urlMod: container
             ) {
            if(urlModel.equals(urlMod))
                return true;

        }
        return false;
    }
    public List<URLModel> getURLsByAccount (String accountId)
    {
        List<URLModel> urls = new ArrayList<>();
        for (URLModel url: container
             ) {
            if(url.getAccountId().equals(accountId))
                urls.add(url);
        }
        return urls;
    }
}
