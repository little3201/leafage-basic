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

import java.util.Objects;

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
    public Mono<GroupVO> fetchById(String groupId) {
        return fetchByGroupId(groupId).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        GroupInfo info = new GroupInfo();
        BeanUtils.copyProperties(groupDTO, info);
        return groupInfoRepository.save(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String groupId, GroupDTO groupDTO) {
        GroupInfo info = new GroupInfo();
        BeanUtils.copyProperties(groupDTO, info);
        info.setGroupId(groupId);
        return groupInfoRepository.save(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<Void> removeById(String groupId) {
        return fetchByGroupId(groupId).flatMap(groupInfo -> groupInfoRepository.deleteById(groupInfo.getId()));
    }

    /**
     * 根据ID查询
     *
     * @param groupId 组ID
     * @return GroupInfo 对象
     */
    private Mono<GroupInfo> fetchByGroupId(String groupId) {
        GroupInfo info = new GroupInfo();
        info.setGroupId(groupId);
        return groupInfoRepository.findOne(Example.of(info));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 信息
     * @return GroupVO 输出对象
     */
    private GroupVO convertOuter(GroupInfo info) {
        GroupVO outer = new GroupVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
