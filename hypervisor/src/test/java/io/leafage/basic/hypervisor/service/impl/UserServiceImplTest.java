/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * user service test
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;

    @BeforeEach
    void init() {
        userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setFirstname("三");
        userDTO.setLastname("张");
        userDTO.setDescription("user");
    }


    @Test
    void fetch() {
        given(this.userRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(User.class)));

        UserVO vo = userService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(vo);
    }

    @Test
    void create() {
        given(this.userRepository.saveAndFlush(Mockito.any(User.class))).willReturn(Mockito.mock(User.class));

        UserVO vo = userService.create(userDTO);

        verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(User.class));
        Assertions.assertNotNull(vo);
    }

    @Test
    void modify() {
        // 根据id查询信息
        given(this.userRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(User.class)));

        // 保存更新信息
        given(this.userRepository.save(Mockito.any(User.class))).willReturn(Mockito.mock(User.class));

        UserVO vo = userService.modify(1L, userDTO);

        verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Assertions.assertNotNull(vo);
    }

    @Test
    void exist() {
        given(this.userRepository.exists(Mockito.anyString())).willReturn(Boolean.TRUE);

        boolean exist = userService.exist("test");

        Assertions.assertTrue(exist);
    }

    @Test
    void exist_false() {
        given(this.userRepository.exists(Mockito.anyString())).willReturn(false);

        boolean exist = userService.exist("test");

        Assertions.assertFalse(exist);
    }

    @Test
    void remove() {
        userService.remove(Mockito.anyLong());

        verify(userRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }
}