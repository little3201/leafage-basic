/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.GroupInfo;
import top.abeille.basic.hypervisor.repository.GroupInfoRepository;
import top.abeille.basic.hypervisor.service.GroupInfoService;

import java.util.List;
import java.util.Optional;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupInfoServiceImpl implements GroupInfoService {

    private final GroupInfoRepository groupInfoRepository;

    public GroupInfoServiceImpl(GroupInfoRepository groupInfoRepository) {
        this.groupInfoRepository = groupInfoRepository;
    }

    @Override
    public Mono<GroupInfo> getById(Long id) {
        return groupInfoRepository.findById(id);
    }

    @Override
    public Mono<GroupInfo> save(GroupInfo entity) {
        return groupInfoRepository.save(entity);
    }

    @Override
    public Mono<Void> removeById(Long id) {
        return groupInfoRepository.deleteById(id);
    }
}
