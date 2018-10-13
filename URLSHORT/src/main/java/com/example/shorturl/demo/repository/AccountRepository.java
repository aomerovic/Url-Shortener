package com.example.shorturl.demo.repository;

import com.example.shorturl.demo.model.AccountModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountModel,Long> {
    AccountModel findByAccountId(String accountId);
}
