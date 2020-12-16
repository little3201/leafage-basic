/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import top.abeille.basic.assets.entity.Account;
import top.abeille.basic.assets.repository.AccountRepository;
import top.abeille.basic.assets.vo.AccountVO;
import top.abeille.common.mock.AbstractServiceMock;

import java.util.Optional;

/**
 * description
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
public class AccountServiceImplTest extends AbstractServiceMock {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void fetch() {
        Mockito.when(accountRepository.findOne(Mockito.any())).thenReturn(Optional.of(Mockito.mock(Account.class)));
        AccountVO accountVO = accountService.fetch(Mockito.anyString());
        Assertions.assertNotNull(accountVO);
    }
}