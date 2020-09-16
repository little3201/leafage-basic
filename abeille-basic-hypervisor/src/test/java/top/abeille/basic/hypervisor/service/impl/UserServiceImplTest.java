/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.vo.UserVO;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void save() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("管理员");
        userInfoService.create(userDTO);
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    /**
     * 测试根据业务ID查询用户信息, 正常返回数据
     */
    @Test
    public void fetchByBusinessId_returnObject() {
        String businessId = "US327SDF9";
        Mono<UserVO> userVOMono = userInfoService.fetchByCode(businessId);
        Mockito.when(userRepository.findOne(Example.of(Mockito.any(UserInfo.class)))).thenReturn(Mockito.any());
        Assert.assertNotNull(userVOMono.map(UserVO::getUsername).block());
    }

    /**
     * 测试根据业务ID查询用户信息, 返回空数据
     */
    @Test
    public void fetchByBusinessId_returnEmpty() {
        String businessId = "US327SDF9";
        Mono<UserVO> userVOMono = userInfoService.fetchByCode(businessId);
        Mockito.when(userRepository.findOne(Example.of(Mockito.any(UserInfo.class)))).thenReturn(Mockito.isNull());
        Assert.assertNull(userVOMono.map(UserVO::getUsername).block());
    }
}