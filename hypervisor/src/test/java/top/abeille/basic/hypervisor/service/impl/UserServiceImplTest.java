/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.User;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * 测试新增用户信息
     */
    @Test
    public void create() {
        userService.create(Mockito.mock(UserDTO.class));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    /**
     * 测试新增用户信息
     */
    @Test
    public void createError() {
        Mockito.when(userRepository.save(Mockito.mock(User.class))).thenThrow(new RuntimeException());
        userService.create(Mockito.mock(UserDTO.class));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * 测试查询用户信息, 正常返回数据
     */
    @Test
    public void fetchDetails() {
        String username = Mockito.anyString();
        Mockito.when(userRepository.findByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .thenReturn(Mockito.any());
        Mono<UserDetailsVO> detailsMono = userService.fetchDetails(username);
        Assertions.assertNotNull(detailsMono);
    }

    /**
     * 测试查询用户信息, 返回空数据
     */
    @Test
    public void fetchDetailsEmpty() {
        String username = Mockito.anyString();
        Mockito.when(userRepository.findByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .thenReturn(Mono.empty());
        Mono<UserDetailsVO> detailsMono = userService.fetchDetails(username);
        Assertions.assertNull(detailsMono);
    }
}