/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.repository.RoleRepository;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

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
    public Page<RoleVO> retrieveByPage(Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("is_enabled", exact());
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setEnabled(true);
        Page<RoleInfo> infoPage = roleRepository.findAll(Example.of(roleInfo, matcher), pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public RoleVO fetchByBusinessId(String businessId) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setBusinessId(businessId);
        Optional<RoleInfo> optional = roleRepository.findOne(Example.of(roleInfo));
        if (optional.isEmpty()) {
            return null;
        }
        return this.convertOuter(roleInfo);
    }

    @Override
    public void removeById(String businessId) {
        Optional<RoleInfo> optional = this.fetchInfo(businessId);
        if (optional.isPresent()) {
            RoleInfo info = optional.get();
            roleRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeInBatch(List<RoleDTO> entities) {
    }

    @Override
    public RoleVO create(RoleDTO roleDTO) {
        RoleInfo info = new RoleInfo();
        BeanUtils.copyProperties(roleDTO, info);
        info.setBusinessId(PrefixEnum.RO + this.generateId());
        info.setModifyTime(LocalDateTime.now());
        RoleInfo roleInfo = roleRepository.save(info);
        return this.convertOuter(roleInfo);
    }

    /**
     * 根据业务ID查信息
     *
     * @param businessId 业务ID
     * @return 数据库对象信息
     */
    private Optional<RoleInfo> fetchInfo(String businessId) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setBusinessId(businessId);
        return roleRepository.findOne(Example.of(roleInfo));
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private RoleVO convertOuter(RoleInfo info) {
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(info, roleVO);
        return roleVO;
    }

    @Override
    public RoleInfo findById(long id) {
        return null;
    }
}
