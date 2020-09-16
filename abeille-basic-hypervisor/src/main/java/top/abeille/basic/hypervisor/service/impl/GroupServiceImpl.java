/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.document.GroupInfo;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.repository.GroupRepository;
import top.abeille.basic.hypervisor.service.GroupService;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.AbstractBasicService;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends AbstractBasicService implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Mono<GroupVO> fetchByCode(String code) {
        Asserts.notBlank(code, "code");
        return groupRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        GroupInfo info = new GroupInfo();
        BeanUtils.copyProperties(groupDTO, info);
        info.setCode(PrefixEnum.GP + this.generateId());
        return groupRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String code, GroupDTO groupDTO) {
        return groupRepository.findByCodeAndEnabledTrue(code).flatMap(info -> {
            BeanUtils.copyProperties(groupDTO, info);
            return groupRepository.save(info);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return groupRepository.findByCodeAndEnabledTrue(code).flatMap(groupInfo ->
                groupRepository.deleteById(groupInfo.getId()));
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
