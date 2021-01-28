/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.common.basic.AbstractBasicService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

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
    public Page<RoleVO> retrieves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> infoPage = roleRepository.findAll(pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public RoleVO fetch(String code) {
        Role role = roleRepository.findByCodeAndEnabledTrue(code);
        return this.convertOuter(role);
    }

    @Override
    public void remove(String code) {
        Role role = roleRepository.findByCodeAndEnabledTrue(code);
        roleRepository.deleteById(role.getId());
    }

    @Override
    public RoleVO create(RoleDTO roleDTO) {
        Role info = new Role();
        BeanUtils.copyProperties(roleDTO, info);
        info.setCode(this.generateCode());
        Role role = roleRepository.save(info);
        return this.convertOuter(role);
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

    @Override
    public Role findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
