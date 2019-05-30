/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.repository.RoleInfoDao;
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

    private final RoleInfoDao roleInfoDao;

    public RoleInfoServiceImpl(RoleInfoDao roleInfoDao) {
        this.roleInfoDao = roleInfoDao;
    }

    @Override
    public Page<RoleInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return roleInfoDao.findAll(pageable);
    }

    @Override
    public List<RoleInfo> findAllByExample(RoleInfo roleInfo, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        Example<RoleInfo> example = Example.of(roleInfo, exampleMatcher);
        return roleInfoDao.findAll(example);
    }

    @Override
    public RoleInfo getById(Long id) {
        Optional<RoleInfo> optional = roleInfoDao.findById(id);
        return optional.orElse(null);
    }

    @Override
    public RoleInfo getByExample(RoleInfo roleInfo) {
        Optional<RoleInfo> optional = roleInfoDao.findOne(Example.of(roleInfo));
        //需要对结果做判断，查询结果为null时会报NoSuchElementException
        return optional.orElse(null);
    }

    @Override
    public List<RoleInfo> findAll(Sort sort) {
        return roleInfoDao.findAll(sort);
    }

    @Override
    public void removeById(Long id) {
        roleInfoDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<RoleInfo> entities) {
        roleInfoDao.deleteInBatch(entities);
    }

    @Override
    public RoleInfo save(RoleInfo entity) {
        return roleInfoDao.save(entity);
    }
}
