/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.TreeNode;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<TreeNode> authorityVOList = new ArrayList<>(roles.size());
        roles.stream().filter(role -> role.getSuperior() == null).forEach(r -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setCode(r.getCode());
            treeNode.setName(r.getName());
            treeNode.setChildren(this.addChildren(r, roles));
            authorityVOList.add(treeNode);
        });
        return authorityVOList;
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
        role = roleRepository.save(role);
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

    /**
     * add child node
     *
     * @param superior superior node
     * @param roles    to be build source data
     * @return tree node
     */
    private List<TreeNode> addChildren(Role superior, List<Role> roles) {
        List<TreeNode> voList = new ArrayList<>();
        roles.stream().filter(role -> superior.getId().equals(role.getSuperior()))
                .forEach(r -> {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setCode(r.getCode());
                    treeNode.setName(r.getName());
                    treeNode.setSuperior(superior.getName());
                    treeNode.setChildren(this.addChildren(r, roles));
                    voList.add(treeNode);
                });
        return voList;
    }
}
