/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.repository.RolePermDao;
import top.abeille.basic.authority.entity.RolePerm;
import top.abeille.basic.authority.service.RolePermService;

import java.util.List;

/**
 * 角色权限信息Service实现
 *
 * @author liwenqiang 2018/9/26 11:40
 **/
@Service
public class RolePermServiceImpl implements RolePermService {

    private final RolePermDao rolePermDao;

    public RolePermServiceImpl(RolePermDao rolePermDao) {
        this.rolePermDao = rolePermDao;
    }

    @Override
    public List<RolePerm> findAllByExample(RolePerm rolePerm, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        Example<RolePerm> example = Example.of(rolePerm, exampleMatcher);
        return rolePermDao.findAll(example);
    }

    @Override
    public RolePerm save(RolePerm entity) {
        return rolePermDao.save(entity);
    }

    @Override
    public List<RolePerm> saveAll(List<RolePerm> entities) {
        return rolePermDao.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        rolePermDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<RolePerm> entities) {
        rolePermDao.deleteInBatch(entities);
    }

}
