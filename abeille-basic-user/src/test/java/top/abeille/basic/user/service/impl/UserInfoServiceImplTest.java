/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */

package top.abeille.basic.user.service.impl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.abeille.basic.user.entity.UserInfo;
import top.abeille.basic.user.repository.UserInfoRepository;
import top.abeille.common.mock.AbstractServiceMock;

import java.util.Optional;

/**
 * java类描述
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
public class UserInfoServiceImplTest extends AbstractServiceMock {

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void save() {
        UserInfo user = new UserInfo();
        user.setId(0L);
        user.setUserNameCn("管理员");
        String pwd = new BCryptPasswordEncoder().encode("abeille");
        user.setPassword(pwd);
        userInfoService.save(user);
        Mockito.verify(userInfoRepository, Mockito.times(1)).save(user);
    }

    /**
     * 主键查询用户信息
     * 如果使用jpa的findById()
     */
    @Test
    public void getById() {
        UserInfo user = new UserInfo();
        user.setId(0L);
        Mockito.when(userInfoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        UserInfo userInfo = userInfoService.getById(user.getId());
        Assert.assertThat(userInfo.getId(), Matchers.equalTo(0L));
    }

    /**
     * 条件查询用户信息
     * 如果使用jpa的findOne()
     */
    @Test
    public void getByExample() {
        UserInfo user = new UserInfo();
        user.setId(0L);
        Mockito.when(userInfoRepository.findOne(Example.of(user))).thenReturn(Optional.of(user));
        UserInfo userInfo = userInfoService.getByExample(user);
        Assert.assertThat(userInfo.getId(), Matchers.equalTo(0L));
    }

    /**
     * 查询所有用户信息
     */
    @Test
    public void findAllByPage() {
        Mockito.when(userInfoRepository.findAll(Mockito.any(Pageable.class))).thenReturn(Mockito.any());
    }

    /**
     * 根据主键删除用户信息
     */
    @Test
    public void removeById() {
        Mockito.verify(userInfoRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    /**
     * 批量删除用户信息
     */
    @Test
    public void removeInBatch() {
        Mockito.verify(userInfoRepository, Mockito.times(1)).deleteInBatch(Mockito.anyIterable());
    }
}