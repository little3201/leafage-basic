/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.TreeNode;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 角色信息service 实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends AbstractBasicService implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<RoleVO> retrieve(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(order) ? order : "modifyTime"));
        Page<Role> infoPage = roleRepository.findAll(pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }


    @Override
    public List<TreeNode> tree() {
        List<Role> roles = roleRepository.findByEnabledTrue();
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        return roles.stream().filter(role -> role.getSuperior() == null).map(r -> {
            TreeNode treeNode = new TreeNode(r.getCode(), r.getName());
            treeNode.setChildren(this.children(r, roles));
            return treeNode;
        }).collect(Collectors.toList());
    }

    @Override
    public RoleVO fetch(String code) {
        Role role = roleRepository.getByCodeAndEnabledTrue(code);
        return this.convertOuter(role);
    }

    @Override
    public RoleVO create(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setCode(this.generateCode());
        if (StringUtils.hasText(roleDTO.getSuperior())) {
            Role superior = roleRepository.getByCodeAndEnabledTrue(roleDTO.getSuperior());
            if (superior != null) {
                role.setSuperior(superior.getId());
            }
        }
        role = roleRepository.save(role);
        return this.convertOuter(role);
    }

    @Override
    public RoleVO modify(String code, RoleDTO roleDTO) {
        Role role = roleRepository.getByCodeAndEnabledTrue(code);
        if (role == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        BeanUtils.copyProperties(roleDTO, role);
        if (StringUtils.hasText(roleDTO.getSuperior())) {
            Role superior = roleRepository.getByCodeAndEnabledTrue(roleDTO.getSuperior());
            if (superior != null) {
                role.setSuperior(superior.getId());
            }
        }
        role = roleRepository.saveAndFlush(role);
        return this.convertOuter(role);
    }

    @Override
    public void remove(String code) {
        Role role = roleRepository.getByCodeAndEnabledTrue(code);
        roleRepository.deleteById(role.getId());
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
        return roleVO;
    }

}
