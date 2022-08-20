/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.ValidMessage;

import java.util.NoSuchElementException;

/**
 * account service impl
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountServiceImpl implements AccountService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, AccountRepository accountRepository) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<Page<AccountVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<AccountVO> voFlux = accountRepository.findByEnabledTrue(pageRequest).map(this::convertOuter);

        Mono<Long> count = accountRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<AccountVO> fetch(String username) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        return accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .map(this::convertOuter);
    }

    @Override
    public Mono<AccountVO> create(AccountDTO accountDTO) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDTO, account);
        return accountRepository.insert(account).map(this::convertOuter);
    }

    @Override
    public Mono<AccountVO> modify(String username, AccountDTO accountDTO) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        return accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(account -> {
                    BeanUtils.copyProperties(accountDTO, account);
                    return accountRepository.save(account).map(this::convertOuter);
                });
    }

    @Override
    public Mono<Boolean> unlock(String username) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where("username").is(username)),
                new Update().set("is_account_locked", false), Account.class).map(UpdateResult::wasAcknowledged);
    }

    @Override
    public Mono<Void> remove(String username) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        return accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(account -> accountRepository.deleteById(account.getId()));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param account 对象
     * @return 输出转换后的vo对象
     */
    private AccountVO convertOuter(Account account) {
        AccountVO outer = new AccountVO();
        BeanUtils.copyProperties(account, outer);
        return outer;
    }

}
