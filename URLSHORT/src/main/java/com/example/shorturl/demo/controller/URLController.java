package com.example.shorturl.demo.controller;

import com.example.shorturl.demo.helper.RandomString;
import com.example.shorturl.demo.model.AccountModel;
import com.example.shorturl.demo.model.URLModel;
import com.example.shorturl.demo.service.AccountService;
import com.example.shorturl.demo.service.URLService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        String accountId = accountModel.getAccountId().trim();
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


        return ResponseEntity.ok().body(response);

    }
    @RequestMapping(method = GET, value = "/{shortUrl}")
    public RedirectView redirect(@PathVariable("shortUrl") String shortUrl)
    {

        String baseURL = "http://short.com/";
        System.out.println(baseURL+shortUrl);
        URLModel urlModel = urlService.findByShortURL( baseURL + shortUrl);
        System.out.println("model:" + urlModel.getUrl() + " " + urlModel.getShortUrl());
        if(urlModel != null)
        {
          //  System.out.println(urlModel.getCounter());
            urlModel.setCounter(urlModel.getCounter()+1);
            urlService.replace(urlModel);
            URLModel model = urlService.findByURL(urlModel.getUrl());
          //  System.out.println(model.getCounter() + " " + model.getUrl());
           // URI uri = URI.create( urlModel.getUrl());
            return new RedirectView(urlModel.getUrl());
            //return ResponseEntity.ok().body(uri);
        }
        return new RedirectView(null);

     //  return ResponseEntity.notFound().build();
       // return new RedirectView( "https://www.journaldev.com/8748/spring-security-role-based-access-authorization-example");
    }

    @RequestMapping(method = GET, value = "/help")
    public String help()
    {
        String help = "<style>\n" +
                "    table,th,td\n" +
                "    {\n" +
                "        border:1px solid black;\n" +
                "        border-collapse:collapse;\n" +
                "    }\n" +
                "    th,td\n" +
                "    {\n" +
                "        padding:5px;\n" +
                "    }\n" +
                "</style>\n" +
                "<h3>URL Shortener:</h3>\n" +
                "\n" +
                "<b>Usage</b>\n" +
                "<br/>\n" +
                "\n" +
                "<ol>\n" +
                "\n" +
                "    <li>\n" +
                "        <u><b>Creating account</b></u>\n" +
                "        <br/>HTTP Method: <b>POST</b>\n" +
                "        <br/>URI: <b>/account</b>\n" +
                "        <br/>Request Body: <b>accountId</b>\n" +
                "        <ul>\n" +
                "            <li>Example: {\"accountId\":\"myAccount1\"}</li>\n" +
                "        </ul>\n" +
                "        Response Body: <b>success</b>, <b>description</b>, <b>password</b>\n" +
                "        <ul>\n" +
                "            <li>Example: {\"success\":\"true\",\"description\":\"Your account is opened.\",\"password\":\"xC345Fc0\"}\n" +
                "        </ul>\n" +
                "        Description\n" +
                "        <ul>\n" +
                "            <li>\"accountId\" is mandatory, therefore you have to include it in request.\n" +
                "                <br/>Account registration can be successful or unsuccessful. Successful registration occurs when \"accountId\" is\n" +
                "                unique and not empty.\n" +
                "                Unsuccessful registration occurs when \"accountId\" is empty. In that case you get Error 415. Also, you get Error 409 if \"accountId\"\n" +
                "                already exists.\n" +
                "                After successful registration you get a random generated password which is 8 alphanumeric characters long string.\n" +
                "\n" +
                "        </ul>\n" +
                "    </li>\n" +
                "    <br/>\n" +
                "    <li>\n" +
                "        <u><b>Registering URL</b></u>\n" +
                "        <br/>HTTP Method: <b>POST</b>\n" +
                "        <br/>URI: <b>/register</b>\n" +
                "        <br/>Request Header: <b>Authorization</b>\n" +
                "        <br/>Request Body: <b>url</b>, <b>redirectType</b>\n" +
                "        <ul>\n" +
                "            <li>Example: {\"url\":\"http://stackoverflow.com/questions/1567929/website-safe-data-access-architecture-question?rq=1\",\"redirectType\":\"301\"}</li>\n" +
                "        </ul>\n" +
                "        Response Body: <b>shortUrl</b>\n" +
                "        <ul>\n" +
                "            <li>Example: {\"shortUrl\":\"http://localhost:8080/xYswlE\"}\n" +
                "        </ul>\n" +
                "        Description\n" +
                "        <ul>\n" +
                "            <li>\"url\" is mandatory. \"redirectType\" is not, it can be 301, but if not set, or if it's not 301, then it's 302.\n" +
                "                <br/>You can register an url only if authorization succeed. If authorization fails you get Error 401.\n" +
                "                If url is empty, you get Error 415. </li>\n" +
                "        </ul>\n" +
                "    </li>\n" +
                "    <br/>\n" +
                "    <li>\n" +
                "        <u><b>Fetching statistics</b></u>\n" +
                "        <br/>HTTP Method: <b>GET</b>\n" +
                "        <br/>URI: <b>/statistic/{accountId}</b>\n" +
                "        <br/>Request Header: <b>Authorization</b>\n" +
                "        Response Body: <b>key:value</b>\n" +
                "        <ul>\n" +
                "            <li>Example: {\"http://myweb.com/somelongurl/somedirectory/\":\"10\",\"http://myweb.com/somelongurl1/somedirectory2/\":\"4\",\"http://myweb.com/somelongurl3/somedirectory4/\":\"91\"}\n" +
                "        </ul>\n" +
                "        Description\n" +
                "        <ul>\n" +
                "            <li>To fetch statistics, you have to pass authorization. If authorization fails you get Error 401. If authorization was\n" +
                "                successful you get number of visits for every link registered with your \"accountId\".</li>\n" +
                "        </ul>\n" +
                "    </li>\n" +
                "    <br/>\n" +
                "    <li>\n" +
                "        <u><b>Redirecting</b></u>\n" +
                "        <br/>Description\n" +
                "        <ul>\n" +
                "            Entering shortUrl provided in \"Registering URL\" redirection occurs to registered url and number of calls for that url is incremented by 1.\n" +
                "        </ul>\n" +
                "    </li>\n" +
                "</ol>";


         return help;
    }

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


