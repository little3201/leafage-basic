/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.repository.RoleInfoRepository;
import top.abeille.basic.authority.entity.RoleInfo;
import top.abeille.basic.authority.service.RoleInfoService;

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

    public RoleInfoServiceImpl(RoleInfoRepository roleInfoRepository) {
        this.roleInfoRepository = roleInfoRepository;
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
}
