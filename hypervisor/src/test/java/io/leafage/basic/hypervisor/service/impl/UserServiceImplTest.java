/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

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
    void create() {
        userService.create(Mockito.mock(UserDTO.class));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    /**
     * 测试新增用户信息
     */
    @Test
    void createError() {
        Mockito.when(userRepository.save(Mockito.mock(User.class))).thenThrow(new RuntimeException());
        userService.create(Mockito.mock(UserDTO.class));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * 测试查询用户信息, 正常返回数据
     */
    @Test
    void fetchDetails() {
        String username = Mockito.anyString();
        Mockito.when(userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .thenReturn(Mockito.any());
        Mono<UserDetails> detailsMono = userService.fetchDetails(username);
        Assertions.assertNotNull(detailsMono);
    }

    /**
     * 测试查询用户信息, 返回空数据
     */
    @Test
    void fetchDetailsEmpty() {
        String username = Mockito.anyString();
        Mockito.when(userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .thenReturn(Mono.empty());
        Mono<UserDetails> detailsMono = userService.fetchDetails(username);
        Assertions.assertNull(detailsMono);
    }
}