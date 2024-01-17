/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Privilege;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
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

import java.util.*;

/**
 * privilege service impl.
 *
 * @author liwenqiang 2018/12/17 19:36
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "modifyTime"));
        return privilegeRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public List<TreeNode> tree() {
        List<Privilege> authorities = privilegeRepository.findByEnabledTrue();
        return this.convertTree(authorities);
    }

    @Override
    public PrivilegeVO create(PrivilegeDTO privilegeDTO) {
        Privilege privilege = new Privilege();
        BeanUtils.copyProperties(privilegeDTO, privilege);
        if (privilegeDTO.getSuperiorId() != null) {
            Privilege superior = privilegeRepository.findById(privilegeDTO.getSuperiorId()).orElse(null);
            if (superior != null) {
                privilege.setSuperior(superior.getId());
            }
        }
        privilege = privilegeRepository.saveAndFlush(privilege);
        return this.convertOuter(privilege);
    }

    @Override
    public PrivilegeVO modify(Long id, PrivilegeDTO privilegeDTO) {
        Assert.notNull(id, "id must not be null.");
        Privilege privilege = privilegeRepository.findById(id).orElse(null);
        if (privilege == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        BeanUtils.copyProperties(privilegeDTO, privilege);
        if (privilegeDTO.getSuperiorId() != null) {
            Privilege superior = privilegeRepository.findById(privilegeDTO.getSuperiorId()).orElse(null);
            if (superior != null) {
                privilege.setSuperior(superior.getId());
            }
        }
        privilege = privilegeRepository.save(privilege);
        return this.convertOuter(privilege);
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private PrivilegeVO convertOuter(Privilege info) {
        PrivilegeVO privilegeVO = new PrivilegeVO();
        BeanUtils.copyProperties(info, privilegeVO);
        return privilegeVO;
    }

    /**
     * 转换为TreeNode
     *
     * @param authorities 集合数据
     * @return 树集合
     */
    private List<TreeNode> convertTree(List<Privilege> authorities) {
        if (CollectionUtils.isEmpty(authorities)) {
            return Collections.emptyList();
        }
        return authorities.stream().filter(a -> a.getSuperior() == null).map(a -> {
            TreeNode treeNode = this.constructNode(a.getId(), a);
            Set<String> expand = new HashSet<>();
            expand.add("icon");
            expand.add("path");
            treeNode.setChildren(this.convert(authorities, expand));
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
        TreeNode treeNode = new TreeNode(privilege.getId(), privilege.getName());
        treeNode.setSuperior(superiorId);

        Map<String, Object> expand = new HashMap<>();
        expand.put("icon", privilege.getIcon());
        expand.put("path", privilege.getPath());

        treeNode.setExpand(expand);
        return treeNode;
    }

}
