/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * user service test
 *
 * @author wq li 2019/1/29 17:10
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
    void retrieve() {
        Page<User> page = new PageImpl<>(List.of(Mockito.mock(User.class)));
        given(this.userRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(page);

        Page<UserVO> voPage = userService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
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
        given(this.userRepository.existsByUsername(Mockito.anyString())).willReturn(Boolean.TRUE);

        boolean exist = userService.exist("test");

        Assertions.assertTrue(exist);
    }

    @Test
    void exist_false() {
        given(this.userRepository.existsByUsername(Mockito.anyString())).willReturn(false);

        boolean exist = userService.exist("test");

        Assertions.assertFalse(exist);
    }

    @Test
    void remove() {
        userService.remove(Mockito.anyLong());

        verify(userRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }
}