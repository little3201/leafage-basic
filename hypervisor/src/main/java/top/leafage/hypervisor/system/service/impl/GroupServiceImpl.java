/*
 * Copyright (c) 2024-2026.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.leafage.hypervisor.system.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.jspecify.annotations.NonNull;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.data.domain.TreeNode;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.system.domain.*;
import top.leafage.hypervisor.system.domain.dto.GroupDTO;
import top.leafage.hypervisor.system.domain.vo.GroupVO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.SimplePrivilegeVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.repository.*;
import top.leafage.hypervisor.system.service.GroupService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static top.leafage.common.data.converter.ModelToTreeNodeConverter.toTree;


/**
 * group service impl.
 *
 * @author wq li
 */
@OperationLog("groups")
@Service
public class GroupServiceImpl implements GroupService {

    private static final BeanCopier copier = BeanCopier.create(GroupDTO.class, Group.class, false);

    private final GroupRepository groupRepository;
    private final GroupPrivilegeRepository groupPrivilegeRepository;
    private final PrivilegeRepository privilegeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Constructor for GroupServiceImpl.
     *
     * @param groupRepository a {@link GroupRepository} object
     */
    public GroupServiceImpl(GroupRepository groupRepository, GroupPrivilegeRepository groupPrivilegeRepository, PrivilegeRepository privilegeRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.groupRepository = groupRepository;
        this.groupPrivilegeRepository = groupPrivilegeRepository;
        this.privilegeRepository = privilegeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull GroupVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Group> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            // 添加superiorId的条件
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            if (StringUtils.hasText(filters) && filters.contains("superiorId")) {
                return basePredicate;
            } else {
                return cb.and(basePredicate, cb.isNull(root.get("superiorId")));
            }
        };

        return groupRepository.findAll(spec, pageable)
                .map(GroupVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TreeNode<@NonNull Long>> tree() {
        List<Group> groups = groupRepository.findAll();
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        return toTree(groups);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return groupRepository.findById(id)
                .map(GroupVO::from)
                .orElseThrow(() -> new EntityNotFoundException("group not found: " + id));
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!groupRepository.existsById(id)) {
            throw new EntityNotFoundException("group not found: " + id);
        }
        return groupRepository.enableById(id) > 0;
    }

    @Transactional
    @Override
    public boolean disable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!groupRepository.existsById(id)) {
            throw new EntityNotFoundException("group not found: " + id);
        }
        return groupRepository.disableById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GroupVO create(GroupDTO dto) {
        if (groupRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        Group entity = groupRepository.save(GroupDTO.toEntity(dto));
        return GroupVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GroupVO modify(Long id, GroupDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group existing = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("group not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                groupRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }

        copier.copy(dto, existing, null);
        Group entity = groupRepository.save(existing);
        return GroupVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!groupRepository.existsById(id)) {
            throw new EntityNotFoundException("group not found: " + id);
        }
        groupRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addMembers(Long id, Set<String> usernames) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group group = groupRepository.findById(id).orElseThrow();
        Set<String> existing = group.getMembers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());

        Set<String> collect = usernames.stream().filter(username -> !existing.contains(username)).collect(Collectors.toSet());
        userRepository.findAllByUsernameIn(collect).forEach(group::addMember);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserVO> members(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group group = groupRepository.findWithMembersById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
        return group.getMembers().stream().map(UserVO::from).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void removeMembers(Long id, Set<String> usernames) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group group = groupRepository.findById(id).orElseThrow();
        userRepository.findAllByUsernameIn(usernames).forEach(group::removeMember);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addRoles(Long id, Set<Long> roleIds) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group group = groupRepository.findById(id).orElseThrow();
        Set<Long> existing = group.getRoles()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        Set<Long> collect = roleIds.stream().filter(roleId -> !existing.contains(roleId)).collect(Collectors.toSet());
        roleRepository.findAllById(collect).forEach(group::addRole);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleVO> roles(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group group = groupRepository.findWithRolesById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
        return group.getRoles().stream().map(RoleVO::from).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void removeRoles(Long id, Set<Long> roleIds) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group group = groupRepository.findById(id).orElseThrow();
        roleRepository.findAllById(roleIds).forEach(group::removeRole);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addPrivilege(Long id, Long privilegeId, String action) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Assert.notNull(privilegeId, String.format(_MUST_NOT_BE_NULL, "privilegeId"));

        Privilege priv = privilegeRepository.findById(privilegeId).orElseThrow();
        if (StringUtils.hasText(action) && !priv.getActions().contains(action)) {
            throw new IllegalArgumentException("无效的 action");
        }
        Set<String> actions = StringUtils.hasText(action) ? Set.of(action) : Set.of();

        Optional<GroupPrivilege> existing = groupPrivilegeRepository
                .findByGroupIdAndPrivilegeId(id, privilegeId);
        Group group = groupRepository.findById(id).orElseThrow();
        if (existing.isPresent()) {
            // 已存在时只追加指定 action，避免覆盖同一 privilege 下的其他 action。
            existing.get().addActions(actions);
            groupPrivilegeRepository.save(existing.get());
            group.syncAuthorities();
            groupRepository.save(group);
        } else {
            // 不存在 → 新增
            group.addPrivilege(priv, actions);
            groupRepository.save(group);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SimplePrivilegeVO> privileges(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return groupPrivilegeRepository.findAllByGroupId(id)
                .stream().map(SimplePrivilegeVO::from)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void removePrivilege(Long id, Long privilegeId, String action) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Assert.notNull(privilegeId, String.format(_MUST_NOT_BE_NULL, "privilegeId"));

        Group group = groupRepository.findById(id).orElseThrow();
        Privilege priv = privilegeRepository.findById(privilegeId).orElseThrow();
        if (StringUtils.hasText(action)) {
            if (!priv.getActions().contains(action)) {
                throw new IllegalArgumentException("无效的 action");
            }
            group.removePrivilegeAction(priv, action);
        } else {
            group.removePrivilege(priv);
        }

        groupRepository.save(group);
    }
}
