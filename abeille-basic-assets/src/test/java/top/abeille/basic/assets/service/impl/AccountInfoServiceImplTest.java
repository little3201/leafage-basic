/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.repository.AccountInfoRepository;

/**
 * description
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@RunWith(MockitoJUnitRunner.class)
public class AccountInfoServiceImplTest {

    @Mock
    private AccountInfoRepository accountInfoRepository;

    @InjectMocks
    private AccountInfoServiceImpl accountInfoService;

    @Test
    public void getById() {
        AccountInfo account = new AccountInfo();
        account.setId(0L);
        Mockito.when(accountInfoRepository.findById(Mockito.anyLong())).thenReturn(Mono.empty());
        AccountInfo accountInfo = accountInfoService.getById(0L).block();
        Assert.assertThat(accountInfo, Matchers.notNullValue());
    }
}