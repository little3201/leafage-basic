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
    public Page<RoleVO> retrieve(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(order) ? order : "modify_time"));
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

}
