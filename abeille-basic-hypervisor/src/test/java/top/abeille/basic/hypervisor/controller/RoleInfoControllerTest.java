/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.junit.Test;
import org.mockito.InjectMocks;
import top.abeille.common.mock.AbstractControllerMock;

/**
 * 角色接口测试类
 *
 * @author liwenqiang 2019/9/14 21:52
 **/
public class RoleInfoControllerTest extends AbstractControllerMock {

    @InjectMocks
    private RoleInfoController roleInfoController;

    @Override
    protected Object getController() {
        return roleInfoController;
    }

    @Test
    public void findRoles() {
    }

    @Test
    public void saveRole() {
    }

    @Test
    public void modifyRole() {
    }

    @Test
    public void removeRole() {
    }

}