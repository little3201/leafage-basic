/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
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
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void fetch() {
        User user = new User();
        user.setId(new ObjectId());
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        given(this.accountRepository.getByModifier(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Account.class)));

        StepVerifier.create(accountService.fetch("test")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        User user = new User();
        user.setId(new ObjectId());
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        Account account = new Account();
        account.setId(new ObjectId());
        account.setCode("21612OL34");
        account.setBalance(new BigDecimal("11.23"));
        account.setType('B');
        given(this.accountRepository.insert(Mockito.any(Account.class))).willReturn(Mono.just(account));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setModifier("test");
        StepVerifier.create(accountService.create(accountDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        User user = new User();
        user.setId(new ObjectId());
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        Account account = new Account();
        account.setId(new ObjectId());
        account.setCode("21612OL34");
        account.setBalance(new BigDecimal("11.23"));
        account.setType('B');
        given(this.accountRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        given(this.accountRepository.save(Mockito.any(Account.class))).willReturn(Mono.just(Mockito.mock(Account.class)));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setModifier("test");
        StepVerifier.create(accountService.modify("21612OL34", accountDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Account account = new Account();
        account.setId(new ObjectId());
        given(this.accountRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        given(this.accountRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(accountService.remove("21612OL34")).verifyComplete();
    }
}