package com.example.shorturl.demo.model;

import javax.persistence.*;

@Entity
public class AccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    String accountId;
    String password;

    public AccountModel() {
    }

    public AccountModel(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
