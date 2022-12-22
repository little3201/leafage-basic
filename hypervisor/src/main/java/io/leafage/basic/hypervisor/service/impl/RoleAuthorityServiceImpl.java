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

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.ValidMessage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * role authority service impl
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    private final RoleRepository roleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public RoleAuthorityServiceImpl(RoleRepository roleRepository, RoleAuthorityRepository roleAuthorityRepository,
                                    AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Mono<List<String>> authorities(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(group -> roleAuthorityRepository.findByRoleIdAndEnabledTrue(group.getId()).flatMap(roleAuthority ->
                        authorityRepository.findById(roleAuthority.getAuthorityId()).map(Authority::getCode))
                ).collectList();
    }

    @Override
    public Flux<RoleVO> roles(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return authorityRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(authority -> roleAuthorityRepository.findByAuthorityIdAndEnabledTrue(authority.getId()).flatMap(roleAuthority ->
                        roleRepository.findById(roleAuthority.getRoleId()).map(role -> {
                            RoleVO roleVO = new RoleVO();
                            BeanUtils.copyProperties(role, roleVO);
                            return roleVO;
                        })).switchIfEmpty(Mono.error(NoSuchElementException::new))
                );
    }

    @Override
    public Mono<Boolean> relation(String code, Set<String> authorities) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Assert.notNull(authorities, "authorities is null");
        Flux<RoleAuthority> flux = roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(role -> {
                    RoleAuthority roleAuthority = new RoleAuthority();
                    roleAuthority.setRoleId(role.getId());
                    return authorityRepository.findByCodeInAndEnabledTrue(authorities).map(authority -> {
                                roleAuthority.setAuthorityId(authority.getId());
                                return roleAuthority;
                            }).switchIfEmpty(Mono.error(NoSuchElementException::new))
                            .collectList().flatMapMany(roleAuthorityRepository::saveAll);
                });
        return flux.hasElements();
    }
}
