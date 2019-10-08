/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.GroupInfo;
import top.abeille.basic.hypervisor.repository.GroupInfoRepository;
import top.abeille.basic.hypervisor.service.GroupInfoService;
import top.abeille.basic.hypervisor.vo.enter.GroupEnter;
import top.abeille.basic.hypervisor.vo.outer.GroupOuter;

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
    public Mono<GroupOuter> getById(String id) {
        return groupInfoRepository.findById(id).map(group -> {
            GroupOuter outer = new GroupOuter();
            BeanUtils.copyProperties(group, outer);
            return outer;
        });
    }

    @Override
    public Mono<GroupOuter> save(GroupEnter enter) {
        GroupInfo info = new GroupInfo();
        BeanUtils.copyProperties(enter, info);
        return groupInfoRepository.save(info).map(group -> {
            GroupOuter outer = new GroupOuter();
            BeanUtils.copyProperties(group, outer);
            return outer;
        });
    }

    @Override
    public Mono<Void> removeById(String id) {
        return groupInfoRepository.deleteById(id);
    }
}
