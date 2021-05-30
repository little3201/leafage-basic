/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
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
 * 权限service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class AuthorityServiceImplTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private RoleAuthorityRepository roleAuthorityRepository;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @Test
    void retrieve() {
        given(this.authorityRepository.findByTypeAndEnabledTrue(Mockito.anyChar())).willReturn(Flux.just(Mockito.mock(Authority.class)));
        StepVerifier.create(authorityService.retrieve('M')).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setSuperior(new ObjectId());
        given(this.authorityRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(Flux.just(authority));
        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));
        authority.setName("test");
        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(authority));
        StepVerifier.create(authorityService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(authority));
        authority.setSuperior(new ObjectId());
        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));
        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Authority.class)));
        StepVerifier.create(authorityService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
    }

    @Test
    void modify() {
    }

    @Test
    void tree() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setSuperior(new ObjectId());
        given(this.authorityRepository.findByTypeAndEnabledTrue('M')).willReturn(Flux.just(authority));
        StepVerifier.create(authorityService.tree()).verifyComplete();
    }

    @Test
    void count() {
        given(this.authorityRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(authorityService.count()).expectNextCount(1).verifyComplete();
    }
}