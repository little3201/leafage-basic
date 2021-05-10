/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    void save() {
        UserDTO user = new UserDTO();
        user.setNickname("管理员");
        userInfoService.create(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

}