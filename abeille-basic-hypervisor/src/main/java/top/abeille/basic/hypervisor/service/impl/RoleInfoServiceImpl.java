/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.repository.RoleInfoRepository;
import top.abeille.basic.hypervisor.service.RoleInfoService;

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
    public Flux<RoleInfo> findAllByExample(RoleInfo roleInfo, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        Example<RoleInfo> example = Example.of(roleInfo, exampleMatcher);
        return roleInfoRepository.findAll(example);
    }

    @Override
    public Mono<RoleInfo> getById(Long id) {
        return roleInfoRepository.findById(id);
    }

    @Override
    public Mono<RoleInfo> getByExample(RoleInfo roleInfo) {
        return roleInfoRepository.findOne(Example.of(roleInfo));
    }

    @Override
    public Mono<RoleInfo> save(RoleInfo entity) {
        return roleInfoRepository.save(entity);
    }
}
