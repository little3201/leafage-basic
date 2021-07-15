/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
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
import static org.mockito.Mockito.mock;

/**
 * authority接口测试
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
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setSuperior(new ObjectId());
        given(this.authorityRepository.findByTypeAndEnabledTrue(Mockito.anyChar())).willReturn(Flux.just(authority));

        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        authority.setName("test");
        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(authority));


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

        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Authority.class)));

        StepVerifier.create(authorityService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(mock(Authority.class)));

        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setSuperior(new ObjectId());
        given(this.authorityRepository.insert(Mockito.any(Authority.class))).willReturn(Mono.just(authority));

        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Authority.class)));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setSuperior("21612OL35");
        StepVerifier.create(authorityService.create(authorityDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Authority.class)));

        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setSuperior(new ObjectId());
        given(this.authorityRepository.save(Mockito.any(Authority.class))).willReturn(Mono.just(authority));

        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Authority.class)));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setSuperior("21612OL35");
        StepVerifier.create(authorityService.modify("21612OL34", authorityDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void tree() {
        Authority authority = new Authority();
        ObjectId id = new ObjectId();
        authority.setId(id);
        authority.setCode("21612OL34");
        authority.setName("test");

        Authority child = new Authority();
        child.setId(new ObjectId());
        child.setSuperior(id);
        child.setCode("21612OL35");
        child.setName("test-sub");
        given(this.authorityRepository.findByTypeAndEnabledTrue('M')).willReturn(Flux.just(authority, child));
        StepVerifier.create(authorityService.tree()).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.authorityRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(authorityService.count()).expectNextCount(1).verifyComplete();
    }
}