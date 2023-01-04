/*
 *  Copyright 2018-2023 the original author or authors.
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

import io.leafage.basic.hypervisor.bo.SimpleBO;
import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.document.AccountRole;
import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * authority接口测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class AuthorityServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountRoleRepository accountRoleRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private RoleAuthorityRepository roleAuthorityRepository;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @Test
    void retrieve_page() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setSuperior(new ObjectId());
        given(this.authorityRepository.findByEnabledTrue(Mockito.any(Pageable.class))).willReturn(Flux.just(authority));

        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        authority.setName("test");
        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(authority));

        given(this.authorityRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(authorityService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setSuperior(new ObjectId());
        given(this.authorityRepository.findByTypeAndEnabledTrue('M')).willReturn(Flux.just(authority));

        authority.setName("test");
        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(authority));

        StepVerifier.create(authorityService.retrieve()).expectNextCount(1).verifyComplete();
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
    void fetch_no_superior() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(authority));

        StepVerifier.create(authorityService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Authority.class)));

        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setName("test");
        authority.setType('M');
        authority.setPath("/authority");
        authority.setDescription("description");
        given(this.authorityRepository.insert(Mockito.any(Authority.class))).willReturn(Mono.just(authority));

        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');

        SimpleBO<String> simpleBO = new SimpleBO<>();
        simpleBO.setCode("21612OL35");
        simpleBO.setName("Test");
        authorityDTO.setSuperior(simpleBO);
        StepVerifier.create(authorityService.create(authorityDTO)).expectNextCount(1).verifyComplete();
    }


    @Test
    void create_no_superior() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setName("test");
        authority.setType('M');
        authority.setPath("/authority");
        authority.setDescription("description");
        given(this.authorityRepository.insert(Mockito.any(Authority.class))).willReturn(Mono.just(authority));

        given(this.roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');

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

        SimpleBO<String> simpleBO = new SimpleBO<>();
        simpleBO.setCode("21612OL35");
        simpleBO.setName("Test");
        authorityDTO.setSuperior(simpleBO);
        StepVerifier.create(authorityService.modify("21612OL34", authorityDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void tree() {
        Authority authority = new Authority();
        authority.setId(new ObjectId());
        authority.setCode("21612OL34");
        authority.setName("test");

        Authority child = new Authority();
        child.setId(new ObjectId());
        child.setSuperior(authority.getId());
        child.setCode("21612OL35");
        child.setName("test-sub");
        given(this.authorityRepository.findByEnabledTrue()).willReturn(Flux.just(authority, child));

        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Authority.class)));

        StepVerifier.create(authorityService.tree()).expectNextCount(1).verifyComplete();
    }

    @Test
    void authorities() {
        Account account = new Account();
        account.setId(new ObjectId());
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(account.getId());
        accountRole.setRoleId(new ObjectId());
        given(this.accountRoleRepository.findByAccountIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(accountRole));

        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setRoleId(accountRole.getRoleId());
        roleAuthority.setAuthorityId(new ObjectId());
        given(this.roleAuthorityRepository.findByRoleIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(roleAuthority));

        Authority authority = new Authority();
        authority.setId(roleAuthority.getAuthorityId());
        authority.setCode("21318JO90");
        authority.setName("test");
        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(authority));

        StepVerifier.create(authorityService.authorities("little3201")).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.authorityRepository.existsByName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(authorityService.exist("little3201")).expectNext(Boolean.TRUE).verifyComplete();
    }
}