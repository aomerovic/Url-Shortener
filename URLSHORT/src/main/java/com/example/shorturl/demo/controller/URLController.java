package com.example.shorturl.demo.controller;

import com.example.shorturl.demo.helper.RandomString;
import com.example.shorturl.demo.model.AccountModel;
import com.example.shorturl.demo.model.URLEntity;
import com.example.shorturl.demo.model.URLModel;
import com.example.shorturl.demo.repository.AccountRepository;
import com.example.shorturl.demo.service.AccountService;
import com.example.shorturl.demo.service.URLService;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.apache.coyote.Response;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.swing.text.html.HTML;
import javax.websocket.server.PathParam;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
public class URLController {

    private URLService urlService;
    private AccountService accountService;



    @Autowired
    public URLController(AccountService accountService,
                         URLService urlService)
    {
        this.accountService = accountService;
        this.urlService = urlService;
    }

    @RequestMapping(value = "/account", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAccount(@RequestBody AccountModel accountModel)
    {
        String accountId = accountModel.getAccountId();
        boolean success = true;
        String description = "Your account is opened";
        AccountModel account = new AccountModel();
        account.setAccountId(accountId);

        String response = "";
        AccountModel acModel = accountService.findByAccountId(accountModel.getAccountId());
        if(acModel == null && !"".equals(accountId))
        {
            RandomString gene = new RandomString();
            String generatedString = gene.generateRandomString(8);
            account.setPassword(generatedString);
            accountService.save(account);
            response  =  "{\"success\":\"" + success + "\"description\":\"" + description + "\"password\":\"" + account.getPassword() + "\"}";
        }
        else if("".equals(accountModel.getAccountId()))
        {
            success = false;
            description = "Missing account ID";
            response =  "{\"success\":\"" + success + "\"description\":\"" + description + "\"}";
        }
        else if(accountService.findByAccountId(accountId).getAccountId().equals(accountModel.getAccountId()))
        {
            success = false;
            description = "Account is already opened";
            response =  "{\"success\":\"" + success + "\"description\":\"" + description + "\"}";
        }

        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = POST, value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createURL( @RequestHeader(name = "Authorization") String header, @RequestBody URLModel urlModel)
    {

        String decoded = new String(Base64.decodeBase64(header.substring(6).getBytes()));
        String accountId = decoded.substring(0, decoded.indexOf(":"));
        String password = decoded.substring(decoded.indexOf(":") + 1);

         if(accountService.containsKey(accountId) && accountService.getObject(accountId).getPassword()
                .equals(password))
         {
             return ResponseEntity.status(401).build();
         }

        String baseURL = "http://short.com/";
        String Url = urlModel.getUrl();
        URLModel urlModelNew = new URLModel();

        urlModel.setUrl(Url.trim());
     //   System.out.println(urlModel.getUrl());
      //  System.out.println(urlModelNew.getUrl());

        String response = "";
        if("".equals(Url)) {
            return ResponseEntity.status(415).build();
        }
        urlModel.setAccountId(accountId);
        if (!Integer.valueOf(301).equals(urlModel.getRedirectType()) && !Integer.valueOf(
                301).equals(urlModel.getRedirectType()))
            urlModel.setRedirectType(302);
        urlModelNew = urlService.findByUrl(urlModel.getUrl());
        if(urlModelNew == null || (urlModelNew != null && !urlModelNew.getAccountId().equals(accountId)))
        {
            RandomString gene = new RandomString();
            String generatedString = gene.generateRandomString(6);
            urlModel.setShortUrl(baseURL + generatedString);
            while("".equals(urlService.findByShortUrl(urlModel.getShortUrl())))
            {
                generatedString = gene.generateRandomString(6);
                urlModel.setShortUrl(baseURL + generatedString);
            }

            System.out.println(urlModel.getUrl());
            urlService.save(urlModel);

            response =  "{\"shortUrl\":\"" + urlModel.getShortUrl() + "\"}";
        }
        else
        {
            response = "URL already registered";
        }
//        if (!urlModelNew.getRedirectType().equals(urlModel.getRedirectType()))
//            provider.updateURL(urlModel);

        return ResponseEntity.ok().body(response);

    }
    @RequestMapping(method = GET, value = "/{shortUrl}")
    public ResponseEntity<URI> redirect(@PathVariable("shortUrl") String shortUrl)
    {

        String baseURL = "http://short.com/";
        System.out.println(baseURL+shortUrl);
        URLModel urlModel = urlService.findByShortURL( baseURL + shortUrl);
        System.out.println("model:" + urlModel.getUrl() + " " + urlModel.getShortUrl());
        if(urlModel != null)
        {
            System.out.println(urlModel.getCounter());
            urlModel.setCounter(urlModel.getCounter()+1);
            urlService.replace(urlModel);
            URLModel model = urlService.findByURL(urlModel.getUrl());
            System.out.println(model.getCounter() + " " + model.getUrl());
            URI uri = URI.create( urlModel.getUrl());
            return ResponseEntity.ok().body(uri);
        }

       return ResponseEntity.notFound().build(); // daje samo link
       // return new RedirectView( "https://www.journaldev.com/8748/spring-security-role-based-access-authorization-example");
    }

    @RequestMapping(method = GET, value = "/help")
    public String help()
    {
         String HELP_PAGE = "<style>\n" +
                "table,th,td\n" +
                "{\n" +
                "\tborder:1px solid black;\n" +
                "\tborder-collapse:collapse;\n" +
                "}\n" +
                "th,td\n" +
                "{\n" +
                "\tpadding:5px;\n" +
                "}\n" +
                "</style>\n" +
                "<h3>URL Shortener:</h3>\n" +
                "URL Shortener consists of configuration and user part.\n" +
                "<br/>Configuration serves for:\n" +
                "<ul>\n" +
                "\t<li>Create account</li>\n" +
                "\t<li>Register URL</li>\n" +
                "\t<li>Fetch statistics</li>\n" +
                "</ul>\n" +
                "To access Configuration part use REST call with JSON parameters.\n" +
                "<br/>User part serves for redirection.\n" +
                "<br/>\n" +
                "<br/><b>Installation</b>\n" +
                "<ul>\n" +
                "\t<li>There is no external dependencies, therefore no additional installation is required.</li>\n" +
                "</ul>\n" +
                "<b>Running</b>\n" +
                "<ol>\n" +
                "\t<li>Start a web server.</li>\n" +
                "\t<li>Deploy the service war file to the web server.</li>\n" +
                "</ol>\n" +
                "<b>Usage</b>\n" +
                "<br/>Use a client-side application to access following functionalities. All request and response types are application/json.\n" +
                "<ol>\n" +
                "<li>\n" +
                "\t<u><i>Creating account</i></u>\n" +
                "\t<br/>HTTP Method: <b>POST</b>\n" +
                "\t<br/>URI: <b>/account</b>\n" +
                "\t<br/>Request Body: <b>accountId</b>\n" +
                "\t<ul>\n" +
                "\t\t<li>Example: {\"accountId\":\"myAccount1\"}</li>\n" +
                "\t</ul>\n" +
                "\tResponse Body: <b>success</b>, <b>description</b>, <b>password</b>\n" +
                "\t<ul>\n" +
                "\t\t<li>Example: {\"success\":\"true\",\"description\":\"Your account is opened.\",\"password\":\"xC345Fc0\"}\n" +
                "\t</ul>\n" +
                "\tDescription\n" +
                "\t<ul>\n" +
                "\t\t<li>\"accountId\" is mandatory. It's trimmed and case insensitive.\n" +
                "\t\t<br/>\"success\" can have two values: true and false. False value occurs if \"accountId\" is empty after trimming.\n" +
                "\t\tIn that case status 415 is set. False value also occurs when \"accountId\" already exists. In that case status 409 is set. \n" +
                "\t\tStatus 201 and \"password\" are set only when \"success\" is true. It is random generated 8 alphanumeric characters long string. \n" +
                "\t\t<br/>For each of these cases appropriate description is set.</li>\n" +
                "\t</ul>\n" +
                "</li>\n" +
                "<li>\n" +
                "\t<u><i>Registering URL</i></u>\n" +
                "\t<br/>HTTP Method: <b>POST</b>\n" +
                "\t<br/>URI: <b>/register</b>\n" +
                "\t<br/>Request Header: <b>Authorization</b>\n" +
                "\t<br/>Request Body: <b>url</b>, <b>redirectType</b>\n" +
                "\t<ul>\n" +
                "\t\t<li>Example: {\"url\":\"http://stackoverflow.com/questions/1567929/website-safe-data-access-architecture-question?rq=1\",\"redirectType\":\"301\"}</li>\n" +
                "\t</ul>\n" +
                "\tResponse Body: <b>shortUrl</b>\n" +
                "\t<ul>\n" +
                "\t\t<li>Example: {\"shortUrl\":\"http://localhost:8080/xYswlE\"}\n" +
                "\t</ul>\n" +
                "\tDescription\n" +
                "\t<ul>\n" +
                "\t\t<li>\"url\" is shorten and is mandatory. \"redirectType\" is not, it can be 301, but if set as anything else, then it's 302.\n" +
                "\t\t<br/>\"shortUrl\" is not always set. Response Body is set empty when authorization fails, and for that case status is set to 401.\n" +
                "\t\tResponse Body is also set to empty when \"url\" is empty after trimming. In that case status 415 is set. \n" +
                "\t\tIf \"url\" was already registered it is updated and status is set to 201, else it is created and status is set to 200.</li>\n" +
                "\t</ul>\n" +
                "</li>\n" +
                "<li>\n" +
                "\t<u><i>Fetching statistics</i></u>\n" +
                "\t<br/>HTTP Method: <b>GET</b>\n" +
                "\t<br/>URI: <b>/statistic/{accountId}</b>\n" +
                "\t<br/>Request Header: <b>Authorization</b>\n" +
                "\tResponse Body: <b>key:value</b>\n" +
                "\t<ul>\n" +
                "\t\t<li>Example: {\"http://myweb.com/somelongurl/somedirectory/\":\"10\",\"http://myweb.com/somelongurl1/somedirectory2/\":\"4\",\"http://myweb.com/somelongurl3/somedirectory4/\":\"91\"}\n" +
                "\t</ul>\n" +
                "\tDescription\n" +
                "\t<ul>\n" +
                "\t\t<li>Response Body is set empty when authorization fails or account set in Authorization header differs from the one stated in URI, and for that case status is set to 401.\n" +
                "\t\tIn other cases, status is set to 200, Response body is set to object populated with key:value order pairs for each url that that account registered.\n" +
                "\t\tKey represents registered url, and value represents number of times that url was called.</li>\n" +
                "\t</ul>\n" +
                "</li>\n" +
                "<li>\n" +
                "\t<u><i>Redirecting</i></u>\n" +
                "\t<br/>Description\n" +
                "\t<ul>\n" +
                "\t\tEntering shortUrl provided in \"Registering URL\" redirection occurs to registered url and number of calls for that url is incremented by 1.\n" +
                "\t</ul>\n" +
                "</li>\n" +
                "</ol>";

         return HELP_PAGE;
    }
//    @RequestMapping(method = GET, value = "/statistic/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> getStatistic(@PathVariable("accountid") String accountId)
    @RequestMapping(method = GET, value = "/statistic/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStatistic(@PathVariable("accountId") String accountId, @RequestHeader(name = "Authorization") String header)
    {
        String decoded = new String(Base64.decodeBase64(header.substring(6).getBytes()));
        String accId = decoded.substring(0, decoded.indexOf(":"));
        String password = decoded.substring(decoded.indexOf(":") + 1);

        if(accountService.containsKey(accountId) && accountService.getObject(accountId).getPassword()
                .equals(password))
        {
            return ResponseEntity.status(401).build();
        }

        String response = "{";
        List<URLModel> urls = new ArrayList<>();
        urls = urlService.getURLsByAccount(accId);
        for (URLModel urlModel: urls
             ) {
           // URLModel urlModel = urlService.findByURL("https://www.journaldev.com/8748/spring-security-role-based-access-authorization-example");
            response += urlModel.getUrl() + "/:" + urlModel.getCounter() + "\n";
        }
     //   URLModel urlModel = urlService.findByURL("https://www.journaldev.com/8748/spring-security-role-based-access-authorization-example");
        response += "}";

        return ResponseEntity.ok().body(response);
    }

}


