/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
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
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends ServletAbstractTreeNodeService<Role> implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMembersRepository roleMembersRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMembersRepository roleMembersRepository) {
        this.roleRepository = roleRepository;
        this.roleMembersRepository = roleMembersRepository;
    }

    @Override
    public Page<RoleVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "modifyTime"));
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
        Assert.notNull(id, "id cannot be null.");
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return null;
        }
        return this.convertOuter(role);
    }

    @Override
    public RoleVO create(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role = roleRepository.saveAndFlush(role);
        return this.convertOuter(role);
    }

    @Override
    public RoleVO modify(Long id, RoleDTO roleDTO) {
        Assert.notNull(id, "id cannot be null.");
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        BeanUtils.copyProperties(roleDTO, role);
        role = roleRepository.save(role);
        return this.convertOuter(role);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "id cannot be null.");
        roleRepository.deleteById(id);
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private RoleVO convertOuter(Role info) {
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(info, roleVO);
        long count = roleMembersRepository.countByRoleIdAndEnabledTrue(info.getId());
        roleVO.setCount(count);
        return roleVO;
    }

}
