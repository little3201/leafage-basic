/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

/**
 * 账户service测试
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @MockBean
    private AccountService accountInfoService;

    @Test
    void create() {
        Mockito.when(accountRepository.save(Mockito.any(Account.class)))
                .thenReturn(Mono.just(Mockito.mock(Account.class)));
        accountInfoService.create(Mockito.mock(AccountDTO.class));
        Mockito.verify(accountRepository, Mockito.atLeastOnce()).save(Mockito.any(Account.class));
    }
}