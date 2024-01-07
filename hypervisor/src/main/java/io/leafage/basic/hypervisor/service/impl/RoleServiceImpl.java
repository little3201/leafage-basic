/*
 *  Copyright 2018-2024 the original author or authors.
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

import io.leafage.basic.hypervisor.domain.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

/**
 * role service impl
 *
 * @author liwenqiang 2018-09-27 14:20
 **/
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Flux<RoleVO> retrieve() {
        return roleRepository.findAll().flatMap(this::convertOuter);
    }

    @Override
    public Mono<Page<RoleVO>> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Flux<RoleVO> voFlux = roleRepository.findBy(pageable).flatMap(this::convertOuter);

        Mono<Long> count = roleRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    @Override
    public Mono<Boolean> exist(String roleName) {
        Assert.hasText(roleName, "role name must not be blank.");
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    public Mono<RoleVO> fetch(Long id) {
        Assert.notNull(id, "role id must not be blank.");
        return roleRepository.findById(id).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<RoleVO> create(RoleDTO roleDTO) {
        return Mono.just(roleDTO).map(dto -> {
                    Role role = new Role();
                    BeanUtils.copyProperties(roleDTO, role);
                    return role;
                })
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(roleRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(Long id, RoleDTO roleDTO) {
        Assert.notNull(id, "role id must not be blank.");
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(role -> {
                    BeanUtils.copyProperties(roleDTO, role);
                    return roleRepository.save(role);
                }).flatMap(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param role 信息
     * @return 输出转换后的vo对象
     */
    private Mono<RoleVO> convertOuter(Role role) {
        return Mono.just(role).map(a -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        });
    }

}
