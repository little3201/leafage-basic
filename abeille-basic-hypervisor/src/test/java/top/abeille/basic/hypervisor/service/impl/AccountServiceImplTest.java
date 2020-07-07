/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import top.abeille.basic.hypervisor.document.AccountInfo;
import top.abeille.basic.hypervisor.dto.AccountDTO;
import top.abeille.basic.hypervisor.repository.AccountRepository;

/**
 * 账户接口测试类
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountInfoService;

    @Test
    public void getById() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setType("applePay");
        accountInfoService.create(accountDTO);
        Mockito.verify(accountRepository, Mockito.atLeastOnce()).save(Mockito.any(AccountInfo.class));
    }
}