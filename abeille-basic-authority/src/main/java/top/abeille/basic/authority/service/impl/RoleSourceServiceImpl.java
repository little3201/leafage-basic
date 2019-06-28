/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.entity.RoleSource;
import top.abeille.basic.authority.repository.RoleSourceRepository;
import top.abeille.basic.authority.service.RoleSourceService;

import java.util.List;

/**
 * 角色权限信息Service实现
 *
 * @author liwenqiang 2018/9/26 11:40
 **/
@Service
public class RoleSourceServiceImpl implements RoleSourceService {

    private final RoleSourceRepository roleSourceRepository;

    public RoleSourceServiceImpl(RoleSourceRepository roleSourceRepository) {
        this.roleSourceRepository = roleSourceRepository;
    }

    @Override
    public List<RoleSource> findAllByExample(RoleSource roleSource, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        Example<RoleSource> example = Example.of(roleSource, exampleMatcher);
        return roleSourceRepository.findAll(example);
    }

    @Override
    public RoleSource save(RoleSource entity) {
        return roleSourceRepository.save(entity);
    }

    @Override
    public List<RoleSource> saveAll(List<RoleSource> entities) {
        return roleSourceRepository.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        roleSourceRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<RoleSource> entities) {
        roleSourceRepository.deleteInBatch(entities);
    }

}
