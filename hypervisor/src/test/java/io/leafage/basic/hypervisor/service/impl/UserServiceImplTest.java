/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void retrieve() {
        List<User> users = new ArrayList<>(2);
        Page<User> postsPage = new PageImpl<>(users);
        given(this.userRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(postsPage);

        Page<UserVO> voPage = userService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage);
    }

    @Test
    void fetch() {
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));

        UserVO userVO = userService.fetch("test");

        Assertions.assertNotNull(userVO);
    }

    @Test
    void details() {
        String username = "test";
        given(this.userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .willReturn(Mockito.mock(User.class));

        List<UserRole> userRoleList = new ArrayList<>(1);
        UserRole userRole = new UserRole();
        userRole.setRoleId(1L);
        userRole.setRoleId(1L);
        userRoleList.add(userRole);
        given(this.userRoleRepository.findByUserId(Mockito.anyLong())).willReturn(userRoleList);

        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Role.class)));

        UserDetails userDetails = userService.loadUserByUsername("test");
        Assertions.assertNotNull(userDetails);
    }

    @Test
    void details_null() {
        String username = "test";
        given(this.userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .willReturn(null);

        UserDetails userDetails = userService.loadUserByUsername("test");

        Assertions.assertNull(userDetails);
    }

    @Test
    void create() {
        User user = new User();
        user.setId(1L);
        user.setPassword("123456");
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        given(this.userRepository.save(Mockito.any(User.class))).willReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("管理员");
        userService.create(userDTO);

        verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }


    @Test
    void modify() {
        // 根据code查询信息
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));
        User user = new User();
        user.setId(1L);

        // 保存更新信息
        given(this.userRepository.saveAndFlush(Mockito.any(User.class))).willReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("管理员");
        userService.modify("test", userDTO);

        verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(User.class));
    }

    @Test
    void remove() {
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));

        userService.remove("test");

        verify(this.userRepository, times(1)).saveAndFlush(Mockito.any(User.class));
    }

}