package org.shannon.spring_rest_play.repositories.impl;

import lombok.Synchronized;
import org.shannon.spring_rest_play.Account;
import org.shannon.spring_rest_play.repositories.AccountRepository;
import org.shannon.spring_rest_play.repositories.InMemoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryAccountRepository extends InMemoryRepository<Account, Long> implements AccountRepository {
    private AtomicLong lastId = new AtomicLong(0);
    private final Map<String, Account> accountsByUserName = new HashMap<>();

    @Override
    @Synchronized
    public Optional<Account> findByUsername(String username) {
        return Optional.ofNullable(accountsByUserName.get(username));
    }


    @Override
    protected Account withGeneratedValues(Account entity) {
        return entity.withId(lastId.incrementAndGet());
    }

    @Override
    protected void additionalLookupsAdd(Account entity) {
        accountsByUserName.put(entity.getUsername(), entity);
    }

    @Override
    protected void additionalLookupsRemove(Account entity) {
        accountsByUserName.remove(entity.getUsername());
    }

    @Override
    protected void additionalLookupsClear() {
        accountsByUserName.clear();
    }

    @Override
    protected Long getId(Account entity) {
        return entity.getId();
    }
}
