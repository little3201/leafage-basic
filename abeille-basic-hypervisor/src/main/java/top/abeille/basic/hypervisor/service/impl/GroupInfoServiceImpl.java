/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.document.GroupInfo;
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
    public Mono<GroupVO> fetchById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessId(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        GroupInfo info = new GroupInfo();
        BeanUtils.copyProperties(groupDTO, info);
        return groupInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String businessId, GroupDTO groupDTO) {
        return this.fetchByBusinessId(businessId).flatMap(info -> {
            BeanUtils.copyProperties(groupDTO, info);
            return groupInfoRepository.save(info);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessId(businessId).flatMap(groupInfo -> groupInfoRepository.deleteById(groupInfo.getId()));
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<GroupInfo> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        GroupInfo info = new GroupInfo();
        info.setBusinessId(businessId);
        return groupInfoRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private GroupVO convertOuter(GroupInfo info) {
        GroupVO outer = new GroupVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
