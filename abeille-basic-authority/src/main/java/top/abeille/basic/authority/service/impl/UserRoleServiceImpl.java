/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.repository.UserRoleDao;
import top.abeille.basic.authority.entity.UserRole;
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

    public UserRoleServiceImpl(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Override
    public List<UserRole> findAllByExample(UserRole userRole, ExampleMatcher exampleMatcher) {
        Example<UserRole> example = Example.of(userRole, exampleMatcher);
        return userRoleDao.findAll(example);
    }

    @Override
    public UserRole save(UserRole entity) {
        return userRoleDao.save(entity);
    }

    @Override
    public List<UserRole> findAll(Sort sort) {
        return userRoleDao.findAll(sort);
    }

    @Override
    public List<UserRole> saveAll(List<UserRole> entities) {
        return userRoleDao.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        userRoleDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<UserRole> entities) {
        userRoleDao.deleteInBatch(entities);
    }

    @Override
    public List<UserRole> findAllByUserId(Long userId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(String.valueOf(userId), exact());
        return this.findAllByExample(userRole, exampleMatcher);
    }
}
