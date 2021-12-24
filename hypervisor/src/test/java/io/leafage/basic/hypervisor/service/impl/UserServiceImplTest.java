/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
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

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;

/**
 * user接口测试
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
    void retrieve() {
        given(this.userRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(Flux.just(Mockito.mock(User.class)));
        StepVerifier.create(userService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
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
        User user = new User();
        user.setUsername("leafage");
        user.setFirstname("li");
        user.setLastname("wq");
        user.setPhone("18710023032");
        user.setEmail("test@leafage.top");
        user.setBirthday(LocalDate.now());
        user.setCountry("China");
        user.setProvince("shaanxi");
        user.setCity("xi'an");
        user.setRegion("yanta");
        user.setAddress("科技路");
        user.setEthnicity("han");
        given(this.userRepository.insert(Mockito.any(User.class))).willReturn(Mono.just(user));
        StepVerifier.create(userService.create(Mockito.mock(UserDTO.class))).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.userRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(userService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.userRepository.existsByUsernameOrPhoneOrEmail(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(userService.exist("little3201")).expectNext(Boolean.TRUE).verifyComplete();
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