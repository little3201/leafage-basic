/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.AccountGroupRepository;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.basic.ValidMessage;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * group service impl
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends ReactiveAbstractTreeNodeService<Group> implements GroupService {

    private final GroupRepository groupRepository;
    private final AccountGroupRepository accountGroupRepository;
    private final AccountRepository accountRepository;

    public GroupServiceImpl(GroupRepository groupRepository, AccountGroupRepository accountGroupRepository,
                            AccountRepository accountRepository) {
        this.groupRepository = groupRepository;
        this.accountGroupRepository = accountGroupRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Flux<GroupVO> retrieve() {
        return groupRepository.findByEnabledTrue().flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<Page<GroupVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<GroupVO> voFlux = groupRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = groupRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).flatMap(objects ->
                Mono.just(new PageImpl<>(objects.getT1(), pageRequest, objects.getT2())));
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
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return groupRepository.existsByName(name);
    }

    @Override
    public Mono<GroupVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return groupRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        return Mono.just(groupDTO).map(dto -> {
                    Group group = new Group();
                    BeanUtils.copyProperties(groupDTO, group);
                    group.setCode(this.generateCode());
                    return group;
                })
                .flatMap(group -> this.superior(groupDTO.getSuperior(), group))
                .flatMap(group -> this.principal(groupDTO.getPrincipal(), group))
                .flatMap(groupRepository::insert).flatMap(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(String code, GroupDTO groupDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return groupRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(group -> BeanUtils.copyProperties(groupDTO, group))
                .flatMap(group -> this.superior(groupDTO.getSuperior(), group))
                .flatMap(group -> this.principal(groupDTO.getPrincipal(), group))
                .flatMap(groupRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return groupRepository.getByCodeAndEnabledTrue(code).flatMap(group ->
                groupRepository.deleteById(group.getId()));
    }

    /**
     * principal 转换
     *
     * @param principal 代码
     * @param g         对象
     * @return 转换principal后的对象
     */
    private Mono<Group> principal(String principal, Group g) {
        return Mono.just(g).flatMap(group -> {
            if (StringUtils.hasText(principal)) {
                return accountRepository.getByUsernameAndEnabledTrue(principal).map(user -> {
                    group.setPrincipal(user.getId());
                    return group;
                }).switchIfEmpty(Mono.error(new NoSuchElementException()));
            }
            return Mono.just(group);
        });
    }

    /**
     * superior 转换
     *
     * @param superior 代码
     * @param g        对象
     * @return 转换superior后的对象
     */
    private Mono<Group> superior(String superior, Group g) {
        return Mono.just(g).flatMap(group -> {
            if (StringUtils.hasText(superior)) {
                return groupRepository.getByCodeAndEnabledTrue(superior).map(s -> {
                    group.setSuperior(s.getId());
                    return group;
                }).switchIfEmpty(Mono.error(new NoSuchElementException()));
            }
            return Mono.just(group);
        });
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param group 信息
     * @return 输出转换后的vo对象
     */
    private Mono<GroupVO> convertOuter(Group group) {
        return Mono.just(group).map(g -> {
                    GroupVO groupVO = new GroupVO();
                    BeanUtils.copyProperties(g, groupVO);
                    return groupVO;
                }).flatMap(groupVO -> accountGroupRepository.countByGroupIdAndEnabledTrue(group.getId())
                        .switchIfEmpty(Mono.just(0L)).map(count -> {
                            groupVO.setCount(count);
                            return groupVO;
                        })).flatMap(vo -> this.convertPrincipal(group.getPrincipal(), vo))
                .flatMap(vo -> this.convertSuperior(group.getSuperior(), vo));
    }


    /**
     * principal 转换
     *
     * @param principal 主键
     * @param vo        对象
     * @return 转换principal后的对象
     */
    private Mono<GroupVO> convertPrincipal(ObjectId principal, GroupVO vo) {
        return Mono.just(vo).flatMap(v -> {
            if (principal != null) {
                return accountRepository.findById(principal).map(account -> {
                    v.setPrincipal(account.getNickname());
                    return v;
                }).switchIfEmpty(Mono.just(v));
            }
            return Mono.just(v);
        });
    }

    /**
     * superior 转换
     *
     * @param superior 主键
     * @param vo       对象
     * @return 转换superior后的对象
     */
    private Mono<GroupVO> convertSuperior(ObjectId superior, GroupVO vo) {
        return Mono.just(vo).flatMap(v -> {
            if (superior != null) {
                return groupRepository.findById(superior).map(s -> {
                    v.setSuperior(s.getName());
                    return v;
                }).switchIfEmpty(Mono.just(v));
            }
            return Mono.just(v);
        });
    }

    /**
     * 对象转换为输出结果对象(单条查询)
     *
     * @param group 信息
     * @return 输出转换后的vo对象
     */
    private Mono<GroupVO> fetchOuter(Group group) {
        return Mono.just(group).map(g -> {
                    GroupVO groupVO = new GroupVO();
                    BeanUtils.copyProperties(g, groupVO);
                    return groupVO;
                }).flatMap(vo -> this.convertPrincipal(group.getPrincipal(), vo))
                .flatMap(v -> {
                    if (group.getSuperior() != null) {
                        return groupRepository.findById(group.getSuperior()).map(s -> {
                            v.setPrincipal(s.getName());
                            return v;
                        });
                    }
                    return Mono.just(v);
                });
    }

}
