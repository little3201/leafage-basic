/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
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
    public Mono<GroupOuter> getById(Long groupId) {
        return fetchByGroupId(groupId).map(this::convertOuter);
    }

    @Override
    public Mono<GroupOuter> save(Long groupId, GroupEnter enter) {
        GroupInfo info = new GroupInfo();
        BeanUtils.copyProperties(enter, info);
        return groupInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<Void> removeById(Long groupId) {
        return fetchByGroupId(groupId).flatMap(groupInfo -> groupInfoRepository.deleteById(groupInfo.getId()));
    }

    private Mono<GroupInfo> fetchByGroupId(Long groupId) {
        GroupInfo info = new GroupInfo();
        info.setGroupId(groupId);
        return groupInfoRepository.findOne(Example.of(info));
    }

    private GroupOuter convertOuter(GroupInfo info) {
        GroupOuter outer = new GroupOuter();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
