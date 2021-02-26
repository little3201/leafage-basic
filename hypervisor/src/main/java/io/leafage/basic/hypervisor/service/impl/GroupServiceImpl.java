/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.GroupUserRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.common.basic.AbstractBasicService;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends AbstractBasicService implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final UserRepository userRepository;

    public GroupServiceImpl(GroupRepository groupRepository, GroupUserRepository groupUserRepository,
                            UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<GroupVO> retrieve() {
        return groupRepository.findByEnabledTrue().map(this::convertOuter);
    }

    @Override
    public Flux<GroupVO> retrieve(int page, int size) {
        return groupRepository.findByEnabledTrue(PageRequest.of(page, size))
                .flatMap(group -> groupUserRepository.countByGroupIdAndEnabledTrue(group.getId())
                        .flatMap(count -> {
                            GroupVO groupVO = new GroupVO();
                            BeanUtils.copyProperties(group, groupVO);
                            groupVO.setCount(count);
                            if (group.getSuperior() != null) {
                                return groupRepository.getById(group.getSuperior()).map(superior -> {
                                    groupVO.setSuperior(superior.getName());
                                    return groupVO;
                                });
                            }
                            return Mono.just(groupVO);
                        })
                );
    }

    @Override
    public Mono<GroupVO> fetch(String code) {
        Asserts.notBlank(code, "code");
        return groupRepository.getByCodeAndEnabledTrue(code)
                .flatMap(group -> groupUserRepository.countByGroupIdAndEnabledTrue(group.getId())
                        .flatMap(count -> {
                            GroupVO groupVO = new GroupVO();
                            BeanUtils.copyProperties(group, groupVO);
                            groupVO.setCount(count);
                            if (group.getSuperior() != null) {
                                return groupRepository.getById(group.getSuperior()).map(superior -> {
                                    groupVO.setSuperior(superior.getCode());
                                    return groupVO;
                                });
                            }
                            return Mono.just(groupVO);
                        })
                );
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        Mono<Group> groupMono;
        Group info = new Group();
        BeanUtils.copyProperties(groupDTO, info);
        info.setCode(this.generateCode());
        if (StringUtils.hasLength(groupDTO.getSuperior())) {
            groupMono = groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior())
                    .switchIfEmpty(Mono.error(NotContextException::new))
                    .map(superior -> {
                        info.setSuperior(superior.getId());
                        return info;
                    });
        } else {
            groupMono = Mono.just(info);
        }
        return groupMono.flatMap(group -> userRepository.getByUsername(groupDTO.getPrincipal()).map(principal -> {
            group.setSuperior(principal.getId());
            return group;
        })).flatMap(groupRepository::insert).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String code, GroupDTO groupDTO) {
        Asserts.notBlank(code, "code");
        return groupRepository.getByCodeAndEnabledTrue(code).flatMap(info -> {
            BeanUtils.copyProperties(groupDTO, info);
            // 判断是否设置上级
            if (StringUtils.hasText(groupDTO.getSuperior())) {
                groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .map(superior -> {
                            info.setSuperior(superior.getId());
                            return info;
                        });
            }
            return groupRepository.save(info);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return groupRepository.getByCodeAndEnabledTrue(code).flatMap(group ->
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
