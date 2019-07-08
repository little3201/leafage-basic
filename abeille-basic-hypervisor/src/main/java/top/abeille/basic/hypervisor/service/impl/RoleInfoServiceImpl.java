/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.RoleInfoRepository;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.service.UserRoleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 角色信息service 实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleInfoServiceImpl implements RoleInfoService {

    private final RoleInfoRepository roleInfoRepository;

    private final UserRoleService userRoleService;

    private final UserInfoService userInfoService;

    public RoleInfoServiceImpl(RoleInfoRepository roleInfoRepository, UserRoleService userRoleService, UserInfoService userInfoService) {
        this.roleInfoRepository = roleInfoRepository;
        this.userRoleService = userRoleService;
        this.userInfoService = userInfoService;
    }

    @Override
    public Page<RoleInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return roleInfoRepository.findAll(pageable);
    }

    @Override
    public List<RoleInfo> findAllByExample(RoleInfo roleInfo, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        Example<RoleInfo> example = Example.of(roleInfo, exampleMatcher);
        return roleInfoRepository.findAll(example);
    }

    @Override
    public RoleInfo getById(Long id) {
        Optional<RoleInfo> optional = roleInfoRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public RoleInfo getByExample(RoleInfo roleInfo) {
        Optional<RoleInfo> optional = roleInfoRepository.findOne(Example.of(roleInfo));
        //需要对结果做判断，查询结果为null时会报NoSuchElementException
        return optional.orElse(null);
    }

    @Override
    public List<RoleInfo> findAll(Sort sort) {
        return roleInfoRepository.findAll(sort);
    }

    @Override
    public void removeById(Long id) {
        roleInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<RoleInfo> entities) {
        roleInfoRepository.deleteInBatch(entities);
    }

    @Override
    public RoleInfo save(RoleInfo entity) {
        return roleInfoRepository.save(entity);
    }

    @Override
    public List<RoleInfo> findByUserId(String userId) {
        UserInfo userInfo = userInfoService.getByUserId(userId);
        if (null == userInfo) {
            return Collections.emptyList();
        }
        List<UserRole> userRoleList = userRoleService.findAllByUserId(userInfo.getId());
        if (CollectionUtils.isEmpty(userRoleList)) {
            return Collections.emptyList();
        }
        List<RoleInfo> roleInfoList = new ArrayList<>();
        userRoleList.forEach(userRole -> roleInfoList.add(this.getById(userRole.getRoleId())));
        return roleInfoList;
    }
}
