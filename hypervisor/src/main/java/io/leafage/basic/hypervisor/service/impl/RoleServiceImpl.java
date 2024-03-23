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

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * role service impl.
 *
 * @author wq li 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends ServletAbstractTreeNodeService<Role> implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<RoleVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "lastModifiedDate"));
        return roleRepository.findAll(pageable).map(this::convertOuter);
    }


    @Override
    public List<TreeNode> tree() {
        List<Role> roles = roleRepository.findByEnabledTrue();
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        return roles.stream().filter(role -> role.getSuperior() == null).map(r -> {
            TreeNode treeNode = new TreeNode(r.getId(), r.getName());
            treeNode.setChildren(this.convert(roles));
            return treeNode;
        }).toList();
    }

    @Override
    public RoleVO fetch(Long id) {
        Assert.notNull(id, "role id must not be null.");
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return null;
        }
        return this.convertOuter(role);
    }

    @Override
    public RoleVO create(RoleDTO dto) {
        Role role = new Role();
        BeanCopier copier = BeanCopier.create(RoleDTO.class, Role.class, false);
        copier.copy(dto, role, null);

        role = roleRepository.saveAndFlush(role);
        return this.convertOuter(role);
    }

    @Override
    public RoleVO modify(Long id, RoleDTO dto) {
        Assert.notNull(id, "role id must not be null.");
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        BeanCopier copier = BeanCopier.create(RoleDTO.class, Role.class, false);
        copier.copy(dto, role, null);

        role = roleRepository.save(role);
        return this.convertOuter(role);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "role id must not be null.");
        roleRepository.deleteById(id);
    }

    /**
     * 转换对象
     *
     * @param role {@link Role}
     * @return 结果对象
     */
    private RoleVO convertOuter(Role role) {
        RoleVO vo = new RoleVO();
        BeanCopier copier = BeanCopier.create(Role.class, RoleVO.class, false);
        copier.copy(role, vo, null);

        return vo;
    }

}
