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
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.TreeNode;
import top.leafage.common.servlet.ServletAbstractTreeNodeService;

import java.util.*;

/**
 * privilege service impl.
 *
 * @author wq li
 */
@Service
public class PrivilegeServiceImpl extends ServletAbstractTreeNodeService<Privilege> implements PrivilegeService {

    public final RolePrivilegesRepository rolePrivilegesRepository;
    private final PrivilegeRepository privilegeRepository;

    /**
     * <p>Constructor for PrivilegeServiceImpl.</p>
     *
     * @param rolePrivilegesRepository a {@link io.leafage.basic.hypervisor.repository.RolePrivilegesRepository} object
     * @param privilegeRepository      a {@link io.leafage.basic.hypervisor.repository.PrivilegeRepository} object
     */
    public PrivilegeServiceImpl(RolePrivilegesRepository rolePrivilegesRepository, PrivilegeRepository privilegeRepository) {
        this.rolePrivilegesRepository = rolePrivilegesRepository;
        this.privilegeRepository = privilegeRepository;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PrivilegeVO> retrieve(int page, int size, String sortBy, boolean descending) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return privilegeRepository.findAllBySuperiorIdIsNull(pageable).map(this::convert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TreeNode> tree(String username) {
        List<Privilege> privileges = privilegeRepository.findAll();
        return this.convertTree(privileges);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PrivilegeVO> subset(Long superiorId) {
        return privilegeRepository.findBySuperiorId(superiorId).stream().map(this::convert).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrivilegeVO fetch(Long id) {
        Assert.notNull(id, "group id must not be null.");
        Privilege privilege = privilegeRepository.findById(id).orElse(null);
        if (privilege == null) {
            return null;
        }
        return this.convert(privilege);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrivilegeVO create(PrivilegeDTO dto) {
        Privilege privilege = new Privilege();
        BeanCopier copier = BeanCopier.create(PrivilegeDTO.class, Privilege.class, false);
        copier.copy(dto, privilege, null);

        privilege = privilegeRepository.saveAndFlush(privilege);
        return this.convert(privilege);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrivilegeVO modify(Long id, PrivilegeDTO dto) {
        Assert.notNull(id, "privilege id must not be null.");
        Privilege privilege = privilegeRepository.findById(id).orElse(null);
        if (privilege == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        BeanCopier copier = BeanCopier.create(PrivilegeDTO.class, Privilege.class, false);
        copier.copy(dto, privilege, null);

        privilege = privilegeRepository.save(privilege);
        return this.convert(privilege);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long id) {
        Assert.notNull(id, "privilege id must not be null.");

        privilegeRepository.deleteById(id);
    }

    /**
     * 转换对象
     *
     * @param privilege 基础对象
     * @return 结果对象
     */
    private PrivilegeVO convert(Privilege privilege) {
        PrivilegeVO vo = new PrivilegeVO();
        BeanCopier copier = BeanCopier.create(Privilege.class, PrivilegeVO.class, false);
        copier.copy(privilege, vo, null);
        long count = privilegeRepository.countBySuperiorId(privilege.getId());
        vo.setCount(count);
        return vo;
    }

    /**
     * 转换为TreeNode
     *
     * @param privileges 集合数据
     * @return 树集合
     */
    private List<TreeNode> convertTree(List<Privilege> privileges) {
        if (CollectionUtils.isEmpty(privileges)) {
            return Collections.emptyList();
        }
        Set<String> expand = new HashSet<>();
        expand.add("path");
        expand.add("redirect");
        expand.add("component");
        expand.add("icon");
        expand.add("hidden");
        return this.convert(privileges, expand);
    }

}
