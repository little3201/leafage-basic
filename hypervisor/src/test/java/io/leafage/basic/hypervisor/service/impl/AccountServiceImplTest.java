/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * 账户接口测试类
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@SpringBootTest
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountInfoService;

    @Test
    void getById() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(new BigDecimal("12.0"));
        accountInfoService.create(accountDTO);
        Mockito.verify(accountRepository, Mockito.atLeastOnce()).save(Mockito.any(Account.class));
    }
}