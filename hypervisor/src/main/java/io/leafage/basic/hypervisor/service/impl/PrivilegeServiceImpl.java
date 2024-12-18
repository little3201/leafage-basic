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

import io.leafage.basic.hypervisor.domain.Privilege;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * privilege service impl
 *
 * @author wq li
 */
@Service
public class PrivilegeServiceImpl extends ReactiveAbstractTreeNodeService<Privilege> implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    /**
     * <p>Constructor for PrivilegeServiceImpl.</p>
     *
     * @param privilegeRepository a {@link PrivilegeRepository} object
     */
    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<PrivilegeVO>> retrieve(int page, int size, String sortBy, boolean descending) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Flux<PrivilegeVO> voFlux = privilegeRepository.findAllBy(pageable)
                .map(p -> convertToVO(p, PrivilegeVO.class));

        Mono<Long> count = privilegeRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<List<TreeNode>> tree() {
        Flux<Privilege> privilegeFlux = privilegeRepository.findAll();
        return this.convertTree(privilegeFlux);
    }

    @Override
    public Flux<DictionaryVO> subset(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<PrivilegeVO> retrieve(List<Long> ids) {
        Flux<Privilege> flux;
        if (CollectionUtils.isEmpty(ids)) {
            flux = privilegeRepository.findAll();
        } else {
            flux = privilegeRepository.findAllById(ids);
        }
        return flux.map(p -> convertToVO(p, PrivilegeVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<PrivilegeVO> fetch(Long id) {
        Assert.notNull(id, "id must not be null.");
        return privilegeRepository.findById(id).map(p -> convertToVO(p, PrivilegeVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> exists(String name, Long id) {
        Assert.hasText(name, "name must not be empty.");
        return privilegeRepository.existsByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<PrivilegeVO> create(PrivilegeDTO dto) {
        Privilege privilege = new Privilege();
        BeanUtils.copyProperties(dto, privilege);
        return privilegeRepository.save(privilege).map(p -> convertToVO(p, PrivilegeVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<PrivilegeVO> modify(Long id, PrivilegeDTO dto) {
        Assert.notNull(id, "id must not be null.");
        return privilegeRepository.findById(id).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .map(privilege -> convert(dto, privilege))
                .flatMap(privilegeRepository::save)
                .map(p -> convertToVO(p, PrivilegeVO.class));
    }

    /**
     * convert to TreeNode
     *
     * @param privileges privilege集合
     * @return TreeNode of Flux
     */
    private Mono<List<TreeNode>> convertTree(Flux<Privilege> privileges) {
        Set<String> meta = new HashSet<>();
        meta.add("path");
        meta.add("redirect");
        meta.add("component");
        meta.add("icon");
        meta.add("actions");
        return convertToTree(privileges, meta);
    }

}
