/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.dao.UserRoleDao;
import top.abeille.basic.authority.model.UserRoleModel;
import top.abeille.basic.authority.service.UserRoleService;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 用户角色Service实现
 *
 * @author liwenqiang 2018-12-06 22:05
 **/
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleDao userRoleDao;

    @Autowired
    public UserRoleServiceImpl(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Override
    public List<UserRoleModel> findAllByExample(UserRoleModel userRole, ExampleMatcher exampleMatcher) {
        Example<UserRoleModel> example = Example.of(userRole, exampleMatcher);
        return userRoleDao.findAll(example);
    }

    @Override
    public UserRoleModel save(UserRoleModel entity) {
        return userRoleDao.save(entity);
    }

    @Override
    public List<UserRoleModel> findAll(Sort sort) {
        return userRoleDao.findAll(sort);
    }

    @Override
    public List<UserRoleModel> saveAll(List<UserRoleModel> entities) {
        return userRoleDao.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        userRoleDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<UserRoleModel> entities) {
        userRoleDao.deleteInBatch(entities);
    }

    @Override
    public List<UserRoleModel> findAllByUserId(Long userId) {
        UserRoleModel userRole = new UserRoleModel();
        userRole.setUserId(userId);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(String.valueOf(userId), exact());
        return this.findAllByExample(userRole, exampleMatcher);
    }
}
