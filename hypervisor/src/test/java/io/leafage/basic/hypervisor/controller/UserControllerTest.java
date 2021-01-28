/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.impl.UserServiceImpl;
import io.leafage.common.mock.AbstractControllerMock;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * 用户测试
 *
 * @author liwenqiang 2019/1/29 17:09
 **/
public class UserControllerTest extends AbstractControllerMock<UserController> {

    @Mock
    private UserServiceImpl userInfoService;

    @InjectMocks
    private UserController userController;

    @Override
    protected UserController getController() {
        return userController;
    }

    @Test
    public void findUsers() {

    }

}