/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;

import java.util.Optional;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceImplTest {

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
        user.setNickname("管理员");
        String pwd = new BCryptPasswordEncoder().encode("abeille");
        user.setPassword(pwd);
        Mono<UserInfo> infoMono = userInfoService.save(user);
        Mono<UserInfo> save = Mockito.verify(userInfoRepository, Mockito.times(1)).save(user);
    }


    /**
     * 条件查询用户信息
     * 如果使用jpa的findOne()
     */
    @Test
    public void getByExample() {
        UserInfo user = new UserInfo();
        user.setId(0L);
        Mockito.when(userInfoRepository.findOne(Example.of(user)).blockOptional()).thenReturn(Optional.of(user));
        UserInfo userInfo = userInfoService.getByExample(user).block();
        Assert.assertNotNull(userInfo != null ? userInfo.getId() : null);
    }


}