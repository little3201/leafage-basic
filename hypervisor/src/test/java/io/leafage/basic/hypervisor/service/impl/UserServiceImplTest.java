/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.vo.UserVO;
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
        given(userRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(Flux.just(Mockito.mock(User.class)));
        Flux<UserVO> voFlux = userService.retrieve(0, 2);
        StepVerifier.create(voFlux).expectSubscription().expectNextCount(1).verifyComplete();
    }

    /**
     * 测试新增用户信息
     */
    @Test
    void create() {
        given(userRepository.insert(Mockito.any(User.class))).willReturn(Mono.just(Mockito.mock(User.class)));
        Mono<UserVO> userVOMono = userService.create(Mockito.mock(UserDTO.class));
        StepVerifier.create(userVOMono).verifyError();
    }

    /**
     * 测试新增用户信息
     */
    @Test
    void create_error() {
        given(this.userRepository.insert(Mockito.any(User.class))).willReturn(Mono.empty());
        Mono<UserVO> userVOMono = userService.create(Mockito.mock(UserDTO.class));
        StepVerifier.create(userVOMono).verifyError();
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
        Mono<UserDetails> detailsMono = userService.fetchDetails(username);
        StepVerifier.create(detailsMono).verifyError();
    }
}