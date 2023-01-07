/*
 *  Copyright 2018-2023 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.bo.SimpleBO;
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
import top.leafage.common.TreeNode;
import top.leafage.common.ValidMessage;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * group service impl
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends ReactiveAbstractTreeNodeService<GroupVO> implements GroupService {

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
        return groupRepository.findByEnabledTrue().flatMap(this::convertOuterWithCount)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<Page<GroupVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<GroupVO> voFlux = groupRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuterWithCount);

        Mono<Long> count = groupRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<List<TreeNode>> tree() {
        Flux<GroupVO> groupFlux = groupRepository.findByEnabledTrue()
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(this::convertOuter);
        return this.convert(groupFlux);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return groupRepository.existsByName(name);
    }

    @Override
    public Mono<GroupVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return groupRepository.getByCodeAndEnabledTrue(code).flatMap(this::convertOuter)
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
                .flatMap(groupRepository::insert).flatMap(this::convertOuterWithCount);
    }

    @Override
    public Mono<GroupVO> modify(String code, GroupDTO groupDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return groupRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(group -> BeanUtils.copyProperties(groupDTO, group))
                .flatMap(group -> this.superior(groupDTO.getSuperior(), group))
                .flatMap(group -> this.principal(groupDTO.getPrincipal(), group))
                .flatMap(groupRepository::save).flatMap(this::convertOuterWithCount);
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
    private Mono<Group> superior(SimpleBO<String> superior, Group g) {
        return Mono.just(g).flatMap(group -> {
            if (Objects.nonNull(superior)) {
                return groupRepository.getByCodeAndEnabledTrue(superior.getCode()).map(s -> {
                    group.setSuperior(s.getId());
                    return group;
                }).switchIfEmpty(Mono.error(new NoSuchElementException()));
            }
            return Mono.just(group);
        });
    }

    /**
     * 对象转换为输出结果对象(单条查询)
     *
     * @param group 信息
     * @return 输出转换后的vo对象
     */
    private Mono<GroupVO> convertOuter(Group group) {
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

    /**
     * 对象转换为输出结果对象
     *
     * @param group 信息
     * @return 输出转换后的vo对象
     */
    private Mono<GroupVO> convertOuterWithCount(Group group) {
        return this.convertOuter(group).flatMap(groupVO ->
                accountGroupRepository.countByGroupIdAndEnabledTrue(group.getId())
                        .switchIfEmpty(Mono.just(0L)).map(count -> {
                            groupVO.setCount(count);
                            return groupVO;
                        })).flatMap(vo -> this.convertPrincipal(group.getPrincipal(), vo));
    }


    /**
     * principal 转换
     *
     * @param principalId 主键
     * @param vo          对象
     * @return 转换principal后的对象
     */
    private Mono<GroupVO> convertPrincipal(ObjectId principalId, GroupVO vo) {
        return Mono.just(vo).flatMap(v -> {
            if (principalId != null) {
                return accountRepository.findById(principalId).map(account -> {
                    v.setPrincipal(account.getNickname());
                    return v;
                }).switchIfEmpty(Mono.just(v));
            }
            return Mono.just(v);
        });
    }

}
