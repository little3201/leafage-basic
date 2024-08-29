/*
 *  Copyright 2018-2024 little3201.
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
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;

/**
 * group service impl.
 *
 * @author wq li 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Page<GroupVO> retrieve(int page, int size, String sortBy, boolean descending) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return groupRepository.findAll(pageable).map(this::convert);
    }

    @Override
    public GroupVO create(GroupDTO dto) {
        Group group = new Group();
        BeanCopier copier = BeanCopier.create(GroupDTO.class, Group.class, false);
        copier.copy(dto, group, null);

        group = groupRepository.saveAndFlush(group);
        return this.convert(group);
    }

    @Override
    public GroupVO modify(Long id, GroupDTO dto) {
        Assert.notNull(id, "group id must not be null.");
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        BeanCopier copier = BeanCopier.create(GroupDTO.class, Group.class, false);
        copier.copy(dto, group, null);

        group = groupRepository.save(group);
        return this.convert(group);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "group id must not be null.");
        groupRepository.deleteById(id);
    }

    /**
     * 转换对象
     *
     * @param group 基础对象
     * @return 结果对象
     */
    private GroupVO convert(Group group) {
        GroupVO vo = new GroupVO();
        BeanCopier copier = BeanCopier.create(Group.class, GroupVO.class, false);
        copier.copy(group, vo, null);
        return vo;
    }

}
