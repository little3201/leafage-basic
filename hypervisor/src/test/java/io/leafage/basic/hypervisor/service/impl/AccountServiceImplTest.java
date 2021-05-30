/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * 账户service测试
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void create() {
        given(this.accountRepository.insert(Mockito.any(Account.class))).willReturn(Mono.just(Mockito.mock(Account.class)));
        StepVerifier.create(accountService.create(Mockito.mock(AccountDTO.class))).expectNextCount(1).verifyComplete();
    }
}