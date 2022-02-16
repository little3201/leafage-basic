/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;
import static org.mockito.BDDMockito.given;

/**
 * account接口测试
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void retrieve() {
        given(this.accountRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(Flux.just(Mockito.mock(Account.class)));
        StepVerifier.create(accountService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.accountRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(accountService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Account.class)));

        StepVerifier.create(accountService.fetch("test")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        Account account = new Account();
        account.setId(new ObjectId());
        account.setUsername("leafage");
        account.setPassword("1234567");
        account.setAvatar("./avatar.jpg");
        account.setEnabled(true);
        account.setAccountExpiresAt(LocalDateTime.now().plusDays(7L));
        account.setAccountLocked(false);
        account.setCredentialsExpiresAt(LocalDateTime.now().plusHours(2L));
        given(this.accountRepository.insert(Mockito.any(Account.class))).willReturn(Mono.just(account));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("test");
        StepVerifier.create(accountService.create(accountDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        Account account = new Account();
        account.setId(new ObjectId());
        account.setUsername("leafage");
        account.setPassword("1234567");
        account.setAvatar("./avatar.jpg");
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        given(this.accountRepository.save(Mockito.any(Account.class))).willReturn(Mono.just(Mockito.mock(Account.class)));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("test");
        StepVerifier.create(accountService.modify("21612OL34", accountDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void unlock() {
        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("username").is("test")),
                new Update().set("is_account_locked", false), Account.class)).willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        StepVerifier.create(accountService.unlock("test")).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Account account = new Account();
        account.setId(new ObjectId());
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        given(this.accountRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(accountService.remove("21612OL34")).verifyComplete();
    }
}