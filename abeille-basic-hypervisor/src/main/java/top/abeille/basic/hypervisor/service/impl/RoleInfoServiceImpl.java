/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.GroupInfo;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.document.RoleInfo;
import top.abeille.basic.hypervisor.repository.RoleInfoRepository;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.basic.hypervisor.vo.RoleVO;

import java.util.Objects;

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
    public Flux<RoleVO> retrieveByExample(RoleDTO roleDTO, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        RoleInfo info = new RoleInfo();
        BeanUtils.copyProperties(roleDTO, info);
        return roleInfoRepository.findAll(Example.of(info, exampleMatcher)).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> fetchById(String businessId) {
        return this.fetchByBusinessId(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> create(RoleDTO roleDTO) {
        RoleInfo info = new RoleInfo();
        BeanUtils.copyProperties(roleDTO, info);
        return roleInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(String businessId, RoleDTO roleDTO) {
        return this.fetchByBusinessId(businessId).flatMap(info -> {
            BeanUtils.copyProperties(roleDTO, info);
            return roleInfoRepository.save(info);
        }).map(this::convertOuter);
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<RoleInfo> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        RoleInfo info = new RoleInfo();
        info.setBusinessId(businessId);
        return roleInfoRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private RoleVO convertOuter(RoleInfo info) {
        RoleVO outer = new RoleVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
