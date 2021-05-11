/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.*;
import io.leafage.basic.hypervisor.repository.*;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @Mock
    private RoleAuthorityRepository roleAuthorityRepository;
    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void retrieve() {
        List<User> voList = new ArrayList<>(2);
        Page<User> postsPage = new PageImpl<>(voList);
        given(this.userRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(postsPage);
        Page<UserVO> voPage = userService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage);
    }

    @Test
    void fetch() {
        given(this.userRepository.findByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));
        UserVO userVO = userService.fetch("test");
        Assertions.assertNotNull(userVO);
    }

    @Test
    void fetchDetails() {
        String username = "test";
        given(this.userRepository.findByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .willReturn(Mockito.mock(User.class));

        List<UserRole> userRoleList = new ArrayList<>(1);
        UserRole userRole = new UserRole();
        userRole.setRoleId(1L);
        userRole.setRoleId(1L);
        userRoleList.add(userRole);
        given(this.userRoleRepository.findByUserId(Mockito.anyLong())).willReturn(userRoleList);

        List<RoleAuthority> roleAuthorities = new ArrayList<>(1);
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(1L);
        roleAuthorities.add(roleAuthority);
        given(this.roleAuthorityRepository.findByRoleIdIn(Mockito.anyList())).willReturn(roleAuthorities);


        List<Authority> authorities = new ArrayList<>(1);
        authorities.add(new Authority());
        given(this.authorityRepository.findByIdIn(Mockito.anyList())).willReturn(authorities);

        UserDetails userDetails = userService.fetchDetails("test");
        Assertions.assertNotNull(userDetails);
    }

    @Test
    void fetchDetails_null() {
        String username = "test";
        given(this.userRepository.findByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .willReturn(null);
        UserDetails userDetails = userService.fetchDetails("test");
        Assertions.assertNull(userDetails);
    }

    @Test
    void create() {
        User user = new User();
        user.setId(1L);
        given(this.userRepository.save(Mockito.any(User.class))).willReturn(user);
        List<Role> roles = new ArrayList<>(2);
        roles.add(new Role());
        roles.add(new Role());
        given(this.roleRepository.findByCodeInAndEnabledTrue(Mockito.anyCollection())).willReturn(roles);
        given(this.userRoleRepository.saveAll(Mockito.anyCollection())).willReturn(Mockito.anyList());
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("管理员");
        userDTO.setRoles(Collections.singleton("admin"));
        userService.create(userDTO);
        verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    void modify() {
        // 根据code查询信息
        given(this.userRepository.findByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));
        User user = new User();
        user.setId(1L);
        // 保存更新信息
        given(this.userRepository.saveAndFlush(Mockito.any(User.class))).willReturn(user);
        // 查询角色信息
        List<Role> roles = new ArrayList<>(1);
        Role role = new Role();
        role.setId(1L);
        role.setCode("2119JJ09");
        roles.add(role);
        given(this.roleRepository.findByCodeInAndEnabledTrue(Mockito.anyCollection())).willReturn(roles);
        // 查询用户角色关联信息
        List<UserRole> userRoleList = new ArrayList<>(1);
        UserRole userRole = new UserRole();
        userRole.setRoleId(1L);
        userRole.setRoleId(1L);
        userRoleList.add(userRole);
        given(this.userRoleRepository.findByUserId(Mockito.anyLong())).willReturn(userRoleList);
        // 保存用户角色信息
        given(this.userRoleRepository.saveAll(Mockito.anyCollection())).willReturn(Mockito.anyList());
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("管理员");
        userDTO.setRoles(Collections.singleton("admin"));
        userService.modify("test", userDTO);
        verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        // 一次逻辑删除，一次新增
        verify(userRoleRepository, Mockito.times(2)).saveAll(Mockito.any());
    }

    @Test
    void remove() {
        given(this.userRepository.findByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));
        userService.remove("test");
        verify(this.userRepository, times(1)).saveAndFlush(Mockito.any());
    }

}