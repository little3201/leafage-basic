/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import top.abeille.basic.assets.repository.AccountInfoRepository;
import top.abeille.common.test.AbstractTest;

/**
 * description
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@ExtendWith(AbstractTest.class)
@RunWith(MockitoJUnitRunner.class)
public class AccountInfoServiceImplTest {

    @Mock
    private AccountInfoRepository accountInfoRepository;

    @InjectMocks
    private AccountInfoServiceImpl accountInfoService;

    @Test
    public void getById() {

    }
}