/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Assertions;
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

    @Test
    void fetch() {
        given(this.userRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(User.class)));

        UserVO userVO = userService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(userVO);
    }

    @Test
    void create() {
        User user = new User();
        user.setId(1L);
        given(this.userRepository.saveAndFlush(Mockito.any(User.class))).willReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstname("管理员");
        userService.create(userDTO);

        verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(User.class));
    }

    @Test
    void modify() {
        // 根据id查询信息
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mockito.mock(User.class));

        // 保存更新信息
        given(this.userRepository.save(Mockito.any(User.class))).willReturn(Mockito.mock(User.class));

        userService.modify(Mockito.anyLong(), Mockito.mock(UserDTO.class));

        verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void exist() {
        given(this.userRepository.exists(Mockito.anyString())).willReturn(true);

        boolean exist = userService.exist(Mockito.anyString());

        Assertions.assertTrue(exist);
    }

    @Test
    void exist_false() {
        given(this.userRepository.exists(Mockito.anyString())).willReturn(false);

        boolean exist = userService.exist(Mockito.anyString());

        Assertions.assertFalse(exist);
    }

    @Test
    void remove() {
        User user = new User();
        user.setId(1L);
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(user);

        userService.remove(Mockito.anyLong());

        verify(userRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }
}