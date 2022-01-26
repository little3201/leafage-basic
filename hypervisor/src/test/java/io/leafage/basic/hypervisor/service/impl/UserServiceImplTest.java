/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void fetch() {
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));

        UserVO userVO = userService.fetch("test");

        Assertions.assertNotNull(userVO);
    }

    @Test
    void create() {
        User user = new User();
        user.setId(1L);
        given(this.userRepository.save(Mockito.any(User.class))).willReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstname("管理员");
        userService.create(userDTO);

        verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void modify() {
        // 根据code查询信息
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));
        User user = new User();
        user.setId(1L);
        user.setGender('F');
        user.setLastname("test");

        // 保存更新信息
        given(this.userRepository.saveAndFlush(Mockito.any(User.class))).willReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setLastname("管理员");
        userService.modify("test", userDTO);

        verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(User.class));
    }

    @Test
    void remove() {
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));

        userService.remove("test");

        verify(this.userRepository, times(1)).saveAndFlush(Mockito.any(User.class));
    }

    @Test
    void exist() {
        given(this.userRepository.existsByUsernameOrPhoneOrEmail(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString())).willReturn(true);

        boolean exist = userService.exist("test");

        Assertions.assertTrue(exist);
    }

    @Test
    void exist_false() {
        given(this.userRepository.existsByUsernameOrPhoneOrEmail(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString())).willReturn(false);

        boolean exist = userService.exist("test");

        Assertions.assertFalse(exist);
    }
}