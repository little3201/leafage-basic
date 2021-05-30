/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.domain.TreeNode;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.GroupUserRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.Objects;

/**
 * 分组信息Service 接口实现
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
                            GroupVO groupVO = this.convertOuter(group);
                            groupVO.setCount(count);
                            if (group.getSuperior() != null) {
                                return groupRepository.findById(group.getSuperior()).map(superior -> {
                                    groupVO.setSuperior(superior.getName());
                                    return groupVO;
                                });
                            }
                            if (group.getPrincipal() != null) {
                                return userRepository.findById(group.getPrincipal()).map(principal -> {
                                    groupVO.setPrincipal(principal.getNickname());
                                    return groupVO;
                                });
                            }
                            return Mono.just(groupVO);
                        })
                );
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Group> groupFlux = groupRepository.findByEnabledTrue();
        return groupFlux.filter(g -> Objects.isNull(g.getSuperior())).flatMap(group -> {
            TreeNode treeNode = new TreeNode(group.getCode(), group.getName());
            return this.addChildren(group, groupFlux).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }

    @Override
    public Mono<GroupVO> fetch(String code) {
        Assert.hasText(code, "code is blank");
        return groupRepository.getByCodeAndEnabledTrue(code)
                .flatMap(group -> groupUserRepository.countByGroupIdAndEnabledTrue(group.getId())
                        .flatMap(count -> {
                            GroupVO groupVO = new GroupVO();
                            BeanUtils.copyProperties(group, groupVO);
                            groupVO.setCount(count);
                            if (group.getSuperior() != null) {
                                return groupRepository.findById(group.getSuperior()).map(superior -> {
                                    groupVO.setSuperior(superior.getCode());
                                    return groupVO;
                                });
                            }
                            return Mono.just(groupVO);
                        })
                );
    }

    @Override
    public Mono<Long> count() {
        return groupRepository.count();
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        Group group = new Group();
        BeanUtils.copyProperties(groupDTO, group);
        group.setCode(this.generateCode());
        Mono<Group> groupMono = Mono.just(group);
        if (StringUtils.hasText(groupDTO.getSuperior())) {
            groupMono = groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior()).map(superior -> {
                group.setSuperior(superior.getId());
                return group;
            });
        }
        if (StringUtils.hasText(groupDTO.getPrincipal())) {
            groupMono = userRepository.getByUsername(groupDTO.getPrincipal()).map(principal -> {
                group.setSuperior(principal.getId());
                return group;
            });
        }
        return groupMono.flatMap(groupRepository::insert).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String code, GroupDTO groupDTO) {
        Assert.hasText(code, "code is blank");
        return groupRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(group -> {
                    BeanUtils.copyProperties(groupDTO, group);
                    if (StringUtils.hasText(groupDTO.getSuperior())) {
                        return groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior()).map(superior -> {
                            group.setSuperior(superior.getId());
                            return group;
                        });
                    }
                    if (StringUtils.hasText(groupDTO.getPrincipal())) {
                        return userRepository.getByUsername(groupDTO.getPrincipal()).map(principal -> {
                            group.setSuperior(principal.getId());
                            return group;
                        });
                    }
                    return groupRepository.save(group);
                }).map(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, "code is blank");
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

    /**
     * add child node
     *
     * @param superior  superior node
     * @param groupFlux to be build source data
     * @return tree node
     */
    private Flux<TreeNode> addChildren(Group superior, Flux<Group> groupFlux) {
        return groupFlux.filter(group -> superior.getId().equals(group.getSuperior())).flatMap(group -> {
            TreeNode treeNode = new TreeNode(group.getCode(), group.getName());
            treeNode.setSuperior(superior.getCode());
            return this.addChildren(group, groupFlux).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }
}
