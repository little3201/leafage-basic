/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import top.abeille.basic.hypervisor.service.impl.UserServiceImpl;
import top.abeille.common.mock.AbstractControllerMock;

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