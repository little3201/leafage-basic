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

import io.leafage.basic.hypervisor.domain.Group;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

/**
 * group service impl
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Flux<GroupVO> retrieve() {
        return groupRepository.findAll().flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<Page<GroupVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<GroupVO> voFlux = groupRepository.findBy(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = groupRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<Boolean> exist(String groupName) {
        Assert.hasText(groupName, "group name must not be blank.");
        return groupRepository.existsByGroupName(groupName);
    }

    @Override
    public Mono<GroupVO> fetch(Long id) {
        Assert.notNull(id, "group id must not be null.");
        return groupRepository.findById(id).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<GroupVO> create(GroupDTO groupDTO) {
        return Mono.just(groupDTO).map(dto -> {
                    Group group = new Group();
                    BeanUtils.copyProperties(groupDTO, group);
                    return group;
                })
                .flatMap(groupRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<GroupVO> modify(Long id, GroupDTO groupDTO) {
        Assert.notNull(id, "group id must not be null.");
        return groupRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(group -> BeanUtils.copyProperties(groupDTO, group))
                .flatMap(groupRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "group id must not be null.");
        return groupRepository.deleteById(id);
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
        });
    }
}
