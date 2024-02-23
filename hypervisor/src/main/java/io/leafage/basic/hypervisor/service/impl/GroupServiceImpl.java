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
import io.leafage.basic.hypervisor.repository.GroupMembersRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.TreeNode;
import top.leafage.common.servlet.ServletAbstractTreeNodeService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * group service impl.
 *
 * @author wq li 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends ServletAbstractTreeNodeService<Group> implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembersRepository groupMembersRepository;

    public GroupServiceImpl(GroupRepository groupRepository, GroupMembersRepository groupMembersRepository) {
        this.groupRepository = groupRepository;
        this.groupMembersRepository = groupMembersRepository;
    }

    @Override
    public Page<GroupVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "lastModifiedDate"));
        return groupRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public List<TreeNode> tree() {
        List<Group> groups = groupRepository.findByEnabledTrue();
        if (!CollectionUtils.isEmpty(groups)) {
            return groups.stream().filter(g -> g.getSuperiorId() == null).map(g -> {
                TreeNode treeNode = new TreeNode(g.getId(), g.getName());
                treeNode.setChildren(this.convert(groups));
                return treeNode;
            }).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public GroupVO create(GroupDTO groupDTO) {
        Group group = new Group();
        BeanUtils.copyProperties(groupDTO, group);
        group = groupRepository.saveAndFlush(group);
        return this.convertOuter(group);
    }

    @Override
    public GroupVO modify(Long id, GroupDTO groupDTO) {
        Assert.notNull(id, "group id must not be null.");
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        group = groupRepository.save(group);
        return this.convertOuter(group);
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
    private GroupVO convertOuter(Group group) {
        GroupVO groupVO = new GroupVO();
        BeanUtils.copyProperties(group, groupVO);
        long count = groupMembersRepository.countByGroupIdAndEnabledTrue(group.getId());
        groupVO.setCount(count);
        return groupVO;
    }

}
