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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;

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
                            GroupVO groupVO = new GroupVO();
                            BeanUtils.copyProperties(group, groupVO);
                            groupVO.setCount(count);
                            return Mono.just(groupVO);
                        }).flatMap(groupVO -> groupRepository.findById(group.getSuperior()).map(superior -> {
                            groupVO.setSuperior(superior.getName());
                            return groupVO;
                        }).switchIfEmpty(Mono.just(groupVO))).flatMap(groupVO ->
                                userRepository.findById(group.getPrincipal()).map(user -> {
                                    groupVO.setPrincipal(user.getNickname());
                                    return groupVO;
                                }).switchIfEmpty(Mono.just(groupVO)))
                );
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Group> groupFlux = groupRepository.findByEnabledTrue();
        return groupFlux.filter(g -> g.getSuperior() == null).flatMap(group -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setCode(group.getCode());
            treeNode.setName(group.getName());
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
                            return Mono.just(groupVO);
                        }).flatMap(groupVO -> groupRepository.findById(group.getSuperior()).map(superior -> {
                            groupVO.setSuperior(superior.getCode());
                            return groupVO;
                        }).switchIfEmpty(Mono.just(groupVO)))
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
        return groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior())
                .map(superior -> {
                    group.setSuperior(superior.getId());
                    return group;
                }).switchIfEmpty(Mono.just(group)).flatMap(g -> userRepository.getByUsername(groupDTO.getPrincipal())
                        .map(principal -> {
                            g.setSuperior(principal.getId());
                            return g;
                        }).switchIfEmpty(Mono.just(g)))
                .flatMap(groupRepository::insert).map(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String code, GroupDTO groupDTO) {
        Assert.hasText(code, "code is blank");
        return groupRepository.getByCodeAndEnabledTrue(code).map(group -> {
            BeanUtils.copyProperties(groupDTO, group);
            return group;
        }).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(group -> groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior())
                        .map(superior -> {
                            group.setSuperior(superior.getId());
                            return group;
                        }).switchIfEmpty(Mono.just(group)).flatMap(g ->
                                userRepository.getByUsername(groupDTO.getPrincipal()).map(user -> {
                                    g.setPrincipal(user.getId());
                                    return g;
                                }).switchIfEmpty(Mono.just(g)))
                        .flatMap(groupRepository::save)).map(this::convertOuter);
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
            TreeNode treeNode = new TreeNode();
            treeNode.setCode(group.getCode());
            treeNode.setName(group.getName());
            treeNode.setSuperior(superior.getCode());
            return this.addChildren(group, groupFlux).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }
}
