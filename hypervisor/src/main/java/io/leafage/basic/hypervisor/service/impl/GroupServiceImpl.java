/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserGroupRepository;
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
import top.leafage.common.basic.TreeNode;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 分组信息Service 接口实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends ReactiveAbstractTreeNodeService<Group> implements GroupService {

    private static final String CODE_MESSAGE = "code is blank";

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserGroupRepository userGroupRepository,
                            UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<GroupVO> retrieve() {
        return groupRepository.findByEnabledTrue().flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Flux<GroupVO> retrieve(int page, int size) {
        return groupRepository.findByEnabledTrue(PageRequest.of(page, size)).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Group> groupFlux = groupRepository.findByEnabledTrue()
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
        return groupFlux.filter(g -> !Objects.nonNull(g.getSuperior())).flatMap(group -> {
            TreeNode treeNode = new TreeNode(group.getCode(), group.getName());
            return this.children(group, groupFlux).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }

    @Override
    public Mono<Boolean> exist(String name) {
        return groupRepository.existsByName(name);
    }

    @Override
    public Mono<GroupVO> fetch(String code) {
        Assert.hasText(code, CODE_MESSAGE);
        return groupRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<Long> count() {
        return groupRepository.count();
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        Mono<Group> groupMono = Mono.just(groupDTO).map(dto -> {
            Group group = new Group();
            BeanUtils.copyProperties(groupDTO, group);
            group.setCode(this.generateCode());
            return group;
        });
        // 设置上级
        groupMono = this.principal(groupDTO.getPrincipal(), groupMono);
        // 设置责任人
        groupMono = this.superior(groupDTO.getSuperior(), groupMono);
        return groupMono.flatMap(group -> {
            BeanUtils.copyProperties(groupDTO, group);
            return groupRepository.insert(group);
        }).flatMap(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String code, GroupDTO groupDTO) {
        Assert.hasText(code, CODE_MESSAGE);
        Mono<Group> groupMono = groupRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(group -> BeanUtils.copyProperties(groupDTO, group));
        // 设置上级
        groupMono = this.superior(groupDTO.getSuperior(), groupMono);
        // 设置责任人
        groupMono = this.principal(groupDTO.getPrincipal(), groupMono);
        return groupMono.flatMap(group -> {
            BeanUtils.copyProperties(groupDTO, group);
            return groupRepository.save(group);
        }).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, CODE_MESSAGE);
        return groupRepository.getByCodeAndEnabledTrue(code).flatMap(group ->
                groupRepository.deleteById(group.getId()));
    }

    /**
     * 设置责任人
     *
     * @param principal 责任人username
     * @param groupMono 对象
     * @return 设置责任人后的对象
     */
    private Mono<Group> principal(String principal, Mono<Group> groupMono) {
        if (StringUtils.hasText(principal)) {
            Mono<User> principalMono = userRepository.getByUsername(principal)
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));
            return groupMono.zipWith(principalMono, (g, user) -> {
                g.setPrincipal(user.getId());
                return g;
            });
        }
        return groupMono;
    }

    /**
     * 设置上级
     *
     * @param superior  上级code
     * @param groupMono 当前对象
     * @return 设置上级后的对象
     */
    private Mono<Group> superior(String superior, Mono<Group> groupMono) {
        if (StringUtils.hasText(superior)) {
            Mono<Group> superiorMono = groupRepository.getByCodeAndEnabledTrue(superior)
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));
            return groupMono.zipWith(superiorMono, (g, sup) -> {
                g.setPrincipal(sup.getId());
                return g;
            });
        }
        return groupMono;
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param group 信息
     * @return 输出转换后的vo对象
     */
    private Mono<GroupVO> convertOuter(Group group) {
        Mono<GroupVO> voMono = Mono.just(group).map(g -> {
            GroupVO groupVO = new GroupVO();
            BeanUtils.copyProperties(g, groupVO);
            return groupVO;
        });

        Mono<Long> longMono = userGroupRepository.countByGroupIdAndEnabledTrue(group.getId())
                .switchIfEmpty(Mono.just(0L));
        voMono = voMono.zipWith(longMono, (g, count) -> {
            g.setCount(count);
            return g;
        });

        if (group.getSuperior() != null) {
            Mono<Group> superiorMono = groupRepository.findById(group.getSuperior())
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));
            voMono = voMono.zipWith(superiorMono, (g, superior) -> {
                g.setSuperior(superior.getName());
                return g;
            });
        }
        if (group.getPrincipal() != null) {
            Mono<User> userMono = userRepository.findById(group.getPrincipal())
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));
            voMono = voMono.zipWith(userMono, (g, principal) -> {
                g.setPrincipal(principal.getNickname());
                return g;
            });
        }
        return voMono;
    }

    /**
     * 对象转换为输出结果对象(单条查询)
     *
     * @param group 信息
     * @return 输出转换后的vo对象
     */
    private Mono<GroupVO> fetchOuter(Group group) {
        Mono<GroupVO> voMono = Mono.just(group).map(g -> {
            GroupVO groupVO = new GroupVO();
            BeanUtils.copyProperties(g, groupVO);
            return groupVO;
        });

        if (group.getSuperior() != null) {
            Mono<Group> superiorMono = groupRepository.findById(group.getSuperior())
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));
            voMono = voMono.zipWith(superiorMono, (g, superior) -> {
                g.setSuperior(superior.getCode());
                return g;
            });
        }
        if (group.getPrincipal() != null) {
            Mono<User> userMono = userRepository.findById(group.getPrincipal())
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));
            voMono = voMono.zipWith(userMono, (g, principal) -> {
                g.setPrincipal(principal.getUsername());
                return g;
            });
        }
        return voMono;
    }

}
