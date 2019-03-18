package top.abeille.basic.authority.server.controller;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import top.abeille.basic.authority.BasicControllerTest;
import top.abeille.basic.authority.server.model.UserInfoModel;
import top.abeille.basic.authority.server.service.impl.UserInfoServiceImpl;

/**
 * java类描述
 *
 * @author liwenqiang 2019/1/29 17:09
 **/
public class UserInfoControllerTest extends BasicControllerTest<UserInfoController> {

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
        ResultActions actions = super.getTest("/v1/users", pageMap);
        //验证测试结果
        MockHttpServletResponse response = actions.andReturn().getResponse();
        Assert.assertThat(response.getStatus(), Matchers.equalTo(200));
    }

    @Test
    public void getUser() throws Exception {
        Mockito.when(userInfoService.getById(Mockito.anyLong())).thenReturn(Mockito.any(UserInfoModel.class));
        ResultActions actions = super.getTest("/v1/user", Mockito.anyLong());
        //验证测试结果
        MockHttpServletResponse response = actions.andReturn().getResponse();
        Assert.assertThat(response.getStatus(), Matchers.equalTo(200));
    }

    @Test
    public void saveUser() throws Exception {
        ResultActions actions = super.postTest("/v1/user", Mockito.any(UserInfoModel.class));
        Mockito.verify(userInfoService, Mockito.times(1)).save(Mockito.any(UserInfoModel.class));
        //验证测试结果
        MockHttpServletResponse response = actions.andReturn().getResponse();
        Assert.assertThat(response.getStatus(), Matchers.equalTo(200));
    }

    @Test
    public void modifyUser() throws Exception {
        ResultActions actions = super.putTest("/v1/user", Mockito.any(UserInfoModel.class));
        Mockito.verify(userInfoService, Mockito.times(1)).save(Mockito.any(UserInfoModel.class));
        //验证测试结果
        MockHttpServletResponse response = actions.andReturn().getResponse();
        Assert.assertThat(response.getContentAsString(), Matchers.equalTo(HttpStatus.CREATED.value()));
    }

    @Test
    public void removeUser() throws Exception {
        ResultActions actions = super.deleteTest("/v1/user", Mockito.anyLong());
        Mockito.verify(userInfoService, Mockito.times(1)).removeById(Mockito.anyLong());
        //验证测试结果
        MockHttpServletResponse response = actions.andReturn().getResponse();
        Assert.assertThat(response.getStatus(), Matchers.equalTo(200));
    }

}