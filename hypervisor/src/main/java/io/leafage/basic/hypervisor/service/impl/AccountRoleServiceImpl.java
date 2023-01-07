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

import io.leafage.basic.hypervisor.document.AccountRole;
import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.ValidMessage;

import javax.naming.NotContextException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * account role service impl
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
@Service
public class AccountRoleServiceImpl implements AccountRoleService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;

    public AccountRoleServiceImpl(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository,
                                  RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Flux<AccountVO> accounts(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(role -> accountRoleRepository.findByRoleIdAndEnabledTrue(role.getId()).flatMap(userRole ->
                        accountRepository.findById(userRole.getAccountId()).map(account -> {
                            AccountVO accountVO = new AccountVO();
                            BeanUtils.copyProperties(account, accountVO);
                            return accountVO;
                        })).switchIfEmpty(Mono.error(NoSuchElementException::new))
                );
    }

    @Override
    public Mono<List<String>> roles(String username) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        return accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(account -> accountRoleRepository.findByAccountIdAndEnabledTrue(account.getId())
                        .flatMap(accountRole -> roleRepository.findById(accountRole.getRoleId()).map(Role::getCode))
                ).collectList();
    }

    @Override
    public Mono<Boolean> relation(String username, Set<String> roles) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        Assert.notEmpty(roles, "roles must not empty");
        Flux<AccountRole> flux = accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(user -> {
                    AccountRole accountRole = new AccountRole();
                    accountRole.setAccountId(user.getId());
                    return roleRepository.findByCodeInAndEnabledTrue(roles).switchIfEmpty(Mono.error(NotContextException::new))
                            .map(role -> {
                                accountRole.setRoleId(role.getId());
                                return accountRole;
                            }).switchIfEmpty(Mono.error(NoSuchElementException::new))
                            .collectList().flatMapMany(accountRoleRepository::saveAll);
                });
        return flux.hasElements();
    }
}
