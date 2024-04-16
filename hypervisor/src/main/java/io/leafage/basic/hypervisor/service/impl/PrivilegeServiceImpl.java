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

import java.time.Instant;
import java.util.*;

/**
 * privilege service impl.
 *
 * @author wq li 2018/12/17 19:36
 **/
@Service
public class PrivilegeServiceImpl extends ServletAbstractTreeNodeService<Privilege> implements PrivilegeService {

    public final RolePrivilegesRepository rolePrivilegesRepository;
    private final PrivilegeRepository privilegeRepository;

    public PrivilegeServiceImpl(RolePrivilegesRepository rolePrivilegesRepository, PrivilegeRepository privilegeRepository) {
        this.rolePrivilegesRepository = rolePrivilegesRepository;
        this.privilegeRepository = privilegeRepository;
    }


    @Override
    public Page<PrivilegeVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "lastModifiedDate"));
        return privilegeRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public List<TreeNode> tree() {
        List<Privilege> privileges = privilegeRepository.findAll();
        return this.convertTree(privileges);
    }

    @Override
    public PrivilegeVO create(PrivilegeDTO dto) {
        Privilege privilege = new Privilege();
        BeanCopier copier = BeanCopier.create(PrivilegeDTO.class, Privilege.class, false);
        copier.copy(dto, privilege, null);

        privilege = privilegeRepository.saveAndFlush(privilege);
        return this.convertOuter(privilege);
    }

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
        return this.convertOuter(privilege);
    }

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
    private PrivilegeVO convertOuter(Privilege privilege) {
        PrivilegeVO vo = new PrivilegeVO();
        BeanCopier copier = BeanCopier.create(Privilege.class, PrivilegeVO.class, false);
        copier.copy(privilege, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = privilege.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);
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
        return privileges.stream().filter(a -> a.getSuperiorId() == null).map(a -> {
            TreeNode treeNode = this.constructNode(a.getId(), a);
            Set<String> expand = new HashSet<>();
            expand.add("icon");
            expand.add("path");
            treeNode.setChildren(this.convert(privileges, expand));
            return treeNode;
        }).toList();
    }

    /**
     * construct tree node
     *
     * @param superiorId superior id
     * @param privilege  data
     * @return tree node
     */
    private TreeNode constructNode(Long superiorId, Privilege privilege) {
        Assert.notNull(superiorId, "privilege superior id must not be null.");
        TreeNode treeNode = new TreeNode(privilege.getId(), privilege.getName());
        treeNode.setSuperior(superiorId);

        Map<String, Object> expand = new HashMap<>();
        expand.put("icon", privilege.getIcon());
        expand.put("path", privilege.getPath());

        treeNode.setExpand(expand);
        return treeNode;
    }

}
