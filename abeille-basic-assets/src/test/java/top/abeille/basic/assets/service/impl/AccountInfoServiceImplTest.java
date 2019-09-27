/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.repository.AccountInfoRepository;
import top.abeille.common.mock.AbstractServiceMock;

import java.util.Optional;

/**
 * description
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
public class AccountInfoServiceImplTest extends AbstractServiceMock {

    @Mock
    private AccountInfoRepository accountInfoRepository;

    @InjectMocks
    private AccountInfoServiceImpl accountInfoService;

    @Test
    public void getByAccountId() {
        AccountInfo account = new AccountInfo();
        account.setAccountId("001");
        Mockito.when(accountInfoRepository.findOne(Mockito.any())).thenReturn(Optional.of(account));
        AccountInfo accountInfo = accountInfoService.getByAccountId("001");
        Assert.assertThat(accountInfo, Matchers.notNullValue());
    }
}