package com.example.shorturl.demo.service;

import com.example.shorturl.demo.model.AccountModel;
import com.example.shorturl.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    protected List<AccountModel> container = new ArrayList<>();

    public AccountModel findByAccountId(String accountId)
    {
        return accountRepository.findByAccountId(accountId);
    }
    public void save(AccountModel accountModel)
    {
        accountRepository.save(accountModel);
        container.add(accountModel);
    }
    public AccountModel findByAccountID(String accountId)
    {
        AccountModel accountModel = null;
        for (AccountModel accModel: container
             ) {
            if(accModel.getAccountId().equals(accountId))
                accountModel = accModel;
        }
        return accountModel;
    }

    public boolean containsKey(String key)
    {
        return container.contains(key);
    }
    public AccountModel getObject(String key)
    {
        AccountModel accountModel = new AccountModel();
        for (AccountModel accModel: container
             ) {
            if(accModel.getAccountId().equals(key))
                accountModel = accModel;
        }
        return accountModel;
    }
}
