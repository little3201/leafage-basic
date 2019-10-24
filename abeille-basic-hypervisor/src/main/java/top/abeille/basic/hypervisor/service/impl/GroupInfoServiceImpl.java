/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.entity.GroupInfo;
import top.abeille.basic.hypervisor.repository.GroupInfoRepository;
import top.abeille.basic.hypervisor.service.GroupInfoService;
import top.abeille.basic.hypervisor.vo.GroupVO;

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
    public Mono<GroupVO> queryById(Long groupId) {
        return fetchByGroupId(groupId).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> save(Long groupId, GroupDTO enter) {
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

    private GroupVO convertOuter(GroupInfo info) {
        GroupVO outer = new GroupVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
