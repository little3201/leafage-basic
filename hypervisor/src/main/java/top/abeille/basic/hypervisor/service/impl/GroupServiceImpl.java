/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.Group;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.repository.GroupRepository;
import top.abeille.basic.hypervisor.repository.GroupUserRepository;
import top.abeille.basic.hypervisor.service.GroupService;
import top.abeille.basic.hypervisor.vo.CountVO;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Set;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends AbstractBasicService implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    public GroupServiceImpl(GroupRepository groupRepository, GroupUserRepository groupUserRepository) {
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
    }

    @Override
    public Flux<GroupVO> retrieve(int page, int size) {
        return groupRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> fetch(String code) {
        Asserts.notBlank(code, "code");
        return groupRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Flux<CountVO> countUsers(Set<String> codes) {
        return Flux.fromIterable(codes)
                .flatMap(groupRepository::findByCodeAndEnabledTrue)
                .flatMap(group ->
                        groupUserRepository.countByGroupIdAndEnabledTrue(group.getId()).map(count -> {
                            CountVO countVO = new CountVO();
                            countVO.setCode(group.getCode());
                            countVO.setCount(count);
                            return countVO;
                        })
                );
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        Group info = new Group();
        BeanUtils.copyProperties(groupDTO, info);
        info.setCode(this.generateCode());
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
        return groupRepository.findByCodeAndEnabledTrue(code).flatMap(group ->
                groupRepository.deleteById(group.getId()));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private GroupVO convertOuter(Group info) {
        GroupVO outer = new GroupVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
