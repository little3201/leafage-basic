/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.entity.Group;
import io.leafage.basic.hypervisor.repository.AccountGroupRepository;
import io.leafage.basic.hypervisor.repository.AccountRepository;
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
import top.leafage.common.basic.TreeNode;
import top.leafage.common.basic.ValidMessage;
import top.leafage.common.servlet.ServletAbstractTreeNodeService;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * group service impl.
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends ServletAbstractTreeNodeService<Group> implements GroupService {

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
    public Page<GroupVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "modifyTime"));
        return groupRepository.findAll(pageable).map(this::convertOuter);
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
            Account account = accountRepository.getByUsernameAndEnabledTrue(groupDTO.getPrincipal());
            if (account != null) {
                group.setPrincipal(account.getId());
            }
        }
        if (StringUtils.hasText(groupDTO.getSuperior())) {
            Group superior = groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior());
            if (null == superior) {
                throw new NoSuchElementException("Not Found");
            }
            group.setSuperior(superior.getId());
        }
        group = groupRepository.saveAndFlush(group);
        return this.convertOuter(group);
    }

    @Override
    public GroupVO modify(String code, GroupDTO groupDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Group group = groupRepository.getByCodeAndEnabledTrue(code);
        if (group == null) {
            throw new NoSuchElementException("当前操作数据不存在...");
        }
        BeanUtils.copyProperties(groupDTO, group);
        if (StringUtils.hasText(groupDTO.getSuperior())) {
            Group superior = groupRepository.getByCodeAndEnabledTrue(groupDTO.getSuperior());
            if (superior != null) {
                group.setSuperior(superior.getId());
            }
        }
        if (StringUtils.hasText(groupDTO.getPrincipal())) {
            Account account = accountRepository.getByUsernameAndEnabledTrue(groupDTO.getPrincipal());
            if (account != null) {
                group.setPrincipal(account.getId());
            }
        }
        group = groupRepository.save(group);
        return this.convertOuter(group);
    }

    @Override
    public void remove(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
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
        long count = accountGroupRepository.countByGroupIdAndEnabledTrue(info.getId());
        groupVO.setCount(count);
        return groupVO;
    }

}
