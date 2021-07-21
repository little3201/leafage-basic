/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.entity.Group;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.TreeNode;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 分组信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends AbstractBasicService implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<GroupVO> retrieve(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(order) ? order : "modifyTime"));
        Page<Group> infoPage = groupRepository.findAll(pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public List<TreeNode> tree() {
        List<Group> groups = groupRepository.findByEnabledTrue();
        if (!CollectionUtils.isEmpty(groups)) {
            return groups.stream().filter(g -> g.getSuperior() == null).map(g -> {
                TreeNode treeNode = new TreeNode(g.getCode(), g.getName());
                treeNode.setChildren(this.children(g, groups));
                return treeNode;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public GroupVO create(GroupDTO groupDTO) {
        Group group = new Group();
        BeanUtils.copyProperties(groupDTO, group);
        group.setCode(this.generateCode());
        if (StringUtils.hasText(groupDTO.getPrincipal())) {
            User user = userRepository.getByUsernameAndEnabledTrue(groupDTO.getPrincipal());
            group.setPrincipal(user.getId());
        }
        if (StringUtils.hasText(groupDTO.getSuperior())) {
            Group superior = groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior());
            if (null == superior) {
                throw new NoSuchElementException("Not Found");
            }
            group.setSuperior(superior.getId());
        }
        group = groupRepository.save(group);
        return this.convertOuter(group);
    }

    @Override
    public void remove(String code) {
        Group group = groupRepository.getByCodeAndEnabledTrue(code);
        groupRepository.deleteById(group.getId());
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private GroupVO convertOuter(Group info) {
        GroupVO groupVO = new GroupVO();
        BeanUtils.copyProperties(info, groupVO);
        return groupVO;
    }

}
