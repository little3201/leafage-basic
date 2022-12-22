/*
 *  Copyright 2018-2022 the original author or authors.
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

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.document.AccountGroup;
import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.repository.AccountGroupRepository;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.BDDMockito.given;

/**
 * user-group接口测试
 *
 * @author liwenqiang 2021/6/14 11:10
 **/
@ExtendWith(MockitoExtension.class)
class AccountGroupServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountGroupRepository accountGroupRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private AccountGroupServiceImpl userGroupService;

    @Test
    void users() {
        Group group = new Group();
        ObjectId id = new ObjectId();
        group.setId(id);
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(group));

        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setGroupId(id);
        accountGroup.setAccountId(new ObjectId());
        given(this.accountGroupRepository.findByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(accountGroup));

        given(this.accountRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Account.class)));
        StepVerifier.create(userGroupService.accounts("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void groups() {
        Account account = new Account();
        account.setId(new ObjectId());
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setGroupId(new ObjectId());
        accountGroup.setAccountId(account.getId());
        given(this.accountGroupRepository.findByAccountIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(accountGroup));

        Group group = new Group();
        group.setCode("21612OL34");
        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(group));
        StepVerifier.create(userGroupService.groups("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        Account account = new Account();
        account.setId(new ObjectId());
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        Group group = new Group();
        group.setId(new ObjectId());
        given(this.groupRepository.findByCodeInAndEnabledTrue(Mockito.anyCollection())).willReturn(Flux.just(group));

        given(this.accountGroupRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(AccountGroup.class)));

        StepVerifier.create(userGroupService.relation("little3201", Collections.singleton("21612OL34")))
                .expectNextCount(1).verifyComplete();
    }
}