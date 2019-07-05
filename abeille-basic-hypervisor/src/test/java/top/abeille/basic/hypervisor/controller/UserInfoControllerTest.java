/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */

package top.abeille.basic.hypervisor.controller;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.service.impl.UserInfoServiceImpl;
import top.abeille.common.mock.AbstractControllerMock;

/**
 * 用户测试
 *
 * @author liwenqiang 2019/1/29 17:09
 **/
public class UserInfoControllerTest extends AbstractControllerMock<UserInfoController> {

    @Mock
    private UserInfoServiceImpl userInfoService;

    @InjectMocks
    private UserInfoController userInfoController;

    @Override
    protected UserInfoController getController() {
        return userInfoController;
    }

    @Test
    public void findUsers() throws Exception {
        MultiValueMap<String, String> pageMap = new LinkedMultiValueMap<>();
        pageMap.add("curPage", "1");
        pageMap.add("pageSize", "10");
        Mockito.when(userInfoService.findAllByPage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Page.empty());
        MockHttpServletResponse response = super.getTest("/v1/users", pageMap);
        //验证测试结果
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void getUser() throws Exception {
        Mockito.when(userInfoService.getById(Mockito.anyLong())).thenReturn(Mockito.any(UserInfo.class));
        MockHttpServletResponse response = super.getTest("/v1/hypervisor", Mockito.anyLong());
        //验证测试结果
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void saveUser() throws Exception {
        MockHttpServletResponse response = super.postTest("/v1/hypervisor", Mockito.any(UserInfo.class));
        Mockito.verify(userInfoService, Mockito.times(1)).save(Mockito.any(UserInfo.class));
        //验证测试结果
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpStatus.CREATED.value()));
    }

    @Test
    public void modifyUser() throws Exception {
        MockHttpServletResponse response = super.putTest("/v1/hypervisor", Mockito.any(UserInfo.class));
        Mockito.verify(userInfoService, Mockito.times(1)).save(Mockito.any(UserInfo.class));
        //验证测试结果
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpStatus.ACCEPTED.value()));
    }

    @Test
    public void removeUser() throws Exception {
        MockHttpServletResponse response = super.deleteTest("/v1/hypervisor", Mockito.anyLong());
        Mockito.verify(userInfoService, Mockito.times(1)).removeById(Mockito.anyLong());
        //验证测试结果
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpStatus.MOVED_PERMANENTLY.value()));
    }

}