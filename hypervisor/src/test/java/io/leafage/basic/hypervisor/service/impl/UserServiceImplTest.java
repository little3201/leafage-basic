/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * 用户service测试
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
    private RoleAuthorityRepository roleAuthorityRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void retrieve() {
        given(this.userRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(Flux.just(Mockito.mock(User.class)));
        StepVerifier.create(userService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }


    /**
     * 测试查询用户信息, 正常返回数据
     */
    @Test
    void fetchDetails() {
        String username = "little3201";
        ObjectId userId = new ObjectId();
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        given(this.userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .willReturn(Mono.just(user));
        UserRole userRole = new UserRole();
        ObjectId roleId = new ObjectId();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        given(this.userRoleRepository.findByUserIdAndEnabledTrue(userId))
                .willReturn(Flux.just(userRole));
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(new ObjectId());
        given(this.roleAuthorityRepository.findByRoleIdIn(Mockito.anyList())).willReturn(Flux.just(roleAuthority));
        given(this.authorityRepository.findByIdInAndEnabledTrue(Mockito.anyList())).willReturn(Flux.just(Mockito.mock(Authority.class)));
        StepVerifier.create(userService.fetchDetails(username)).expectNextCount(1).verifyComplete();
    }

    /**
     * 测试查询用户信息, user not found
     */
    @Test
    void fetchDetails_userNotFound() {
        String username = "little3201";
        given(this.userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username))
                .willReturn(Mono.empty());
        StepVerifier.create(userService.fetchDetails(username)).verifyError();
    }

    @Test
    void fetch() {
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(User.class)));
        StepVerifier.create(userService.fetch("little3201")).expectNextCount(1).verifyComplete();
    }

    /**
     * 测试新增用户信息
     */
    @Test
    void create() {
        given(this.userRepository.insert(Mockito.any(User.class))).willReturn(Mono.just(Mockito.mock(User.class)));
        StepVerifier.create(userService.create(Mockito.mock(UserDTO.class))).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.userRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(userService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(User.class)));
        given(this.userRepository.save(Mockito.any(User.class))).willReturn(Mono.just(Mockito.mock(User.class)));
        UserDTO userDTO = new UserDTO();
        StepVerifier.create(userService.modify("little3201", userDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        User user = new User();
        user.setId(new ObjectId());
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));
        given(this.userRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());
        StepVerifier.create(userService.remove("little3201")).verifyComplete();
    }
}