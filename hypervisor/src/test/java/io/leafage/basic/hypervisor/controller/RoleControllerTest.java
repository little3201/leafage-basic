/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 角色接口测试类
 *
 * @author liwenqiang 2019/9/14 21:52
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Test
    public void findRoles() {
    }


}