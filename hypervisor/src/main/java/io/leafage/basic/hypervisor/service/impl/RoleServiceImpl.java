/*
 *  Copyright 2018-2024 little3201.
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * role service impl
 *
 * @author wq li 2018-09-27 14:20
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * <p>Constructor for RoleServiceImpl.</p>
     *
     * @param roleRepository a {@link RoleRepository} object
     */
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<RoleVO>> retrieve(int page, int size, String sortBy, boolean descending) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Flux<RoleVO> voFlux = roleRepository.findAllBy(pageable)
                .map(r -> convertToVO(r, RoleVO.class));

        Mono<Long> count = roleRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<RoleVO> retrieve(List<Long> ids) {
        Flux<Role> flux;
        if (CollectionUtils.isEmpty(ids)) {
            flux = roleRepository.findAll();
        } else {
            flux = roleRepository.findAllById(ids);
        }
        return flux.map(r -> convertToVO(r, RoleVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<RoleVO> fetch(Long id) {
        Assert.notNull(id, "id must not be null.");
        return roleRepository.findById(id).map(r -> convertToVO(r, RoleVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> exists(String name, Long id) {
        Assert.hasText(name, "name must not be empty.");
        return roleRepository.existsByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<RoleVO> create(RoleDTO dto) {
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        return roleRepository.save(role).map(r -> convertToVO(r, RoleVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<RoleVO> modify(Long id, RoleDTO dto) {
        Assert.notNull(id, "id must not be null.");
        return roleRepository.findById(id).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .map(role -> convert(dto, role))
                .flatMap(roleRepository::save)
                .map(r -> convertToVO(r, RoleVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "id must not be null.");
        return roleRepository.deleteById(id);
    }

}
