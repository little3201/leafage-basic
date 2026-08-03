/*
 * Copyright(c) 2019-present the original author or authors.
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
import top.leafage.common.data.core.domain.TreeNode;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.system.domain.*;
import top.leafage.hypervisor.system.domain.dto.GroupDTO;
import top.leafage.hypervisor.system.domain.dto.PrivilegeActionsDTO;
import top.leafage.hypervisor.system.domain.vo.GroupVO;
import top.leafage.hypervisor.system.domain.vo.PrivilegeActionsVO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.repository.*;
import top.leafage.hypervisor.system.service.GroupService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static top.leafage.common.data.core.converter.ModelToTreeNodeConverter.toTree;
import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;
import static top.leafage.hypervisor.constants.GlobalConstant._MUST_NOT_BE_NULL;


/**
 * Group service impl.
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
     * @param groupRepository          a {@link GroupRepository} object
     * @param groupPrivilegeRepository a {@link GroupPrivilegeRepository} object
     * @param privilegeRepository      a {@link PrivilegeRepository} object
     * @param userRepository           a {@link UserRepository} object
     * @param roleRepository           a {@link RoleRepository} object
     */
    public GroupServiceImpl(GroupRepository groupRepository, GroupPrivilegeRepository groupPrivilegeRepository,
                            PrivilegeRepository privilegeRepository, UserRepository userRepository, RoleRepository roleRepository) {
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
    public Page<GroupVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Group> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);

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
                .orElseThrow(() -> new EntityNotFoundException("group not found: " + id));
        return group.getMembers().stream().map(user -> UserVO.from(user, null)).toList();
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
                .orElseThrow(() -> new EntityNotFoundException("group not found: " + id));
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
    public void authorize(Long id, Collection<PrivilegeActionsDTO> dtos) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Group group = groupRepository.findById(id).orElseThrow();

        Set<Long> privilegeIds = dtos.stream()
                .map(PrivilegeActionsDTO::getPrivilegeId)
                .collect(Collectors.toSet());

        // 批量查询 privilege
        Map<Long, Privilege> privileges =
                privilegeRepository.findAllById(privilegeIds)
                        .stream()
                        .collect(Collectors.toMap(Privilege::getId, Function.identity()));

        if (privileges.size() != privilegeIds.size()) {
            throw new IllegalArgumentException(
                    "Invalid privilege"
            );
        }

        // 查出当前 Group 全部已有授权
        List<GroupPrivilege> existingList = groupPrivilegeRepository.findAllByGroupId(id);
        Map<Long, GroupPrivilege> existing = existingList.stream()
                .collect(Collectors.toMap(gp -> gp.getPrivilege().getId(), Function.identity()));

        List<GroupPrivilege> toSave = new ArrayList<>();
        Set<Long> toKeep = new HashSet<>();

        for (PrivilegeActionsDTO dto : dtos) {
            Privilege privilege = privileges.get(dto.getPrivilegeId());
            Set<String> actions = Optional.ofNullable(dto.getActions()).orElse(Collections.emptySet());

            if (!privilege.getActions().containsAll(actions)) {
                throw new IllegalArgumentException("Invalid action: " + actions);
            }

            GroupPrivilege groupPrivilege = existing.get(dto.getPrivilegeId());
            if (groupPrivilege != null) {
                groupPrivilege.updateActions(actions);
            } else {
                groupPrivilege = new GroupPrivilege(group, privilege, actions);
            }
            toSave.add(groupPrivilege);
            toKeep.add(dto.getPrivilegeId());
        }

        // 需要删除的 = 已存在但不在本次列表中的
        List<GroupPrivilege> toDelete = existingList.stream()
                .filter(gp -> !toKeep.contains(gp.getPrivilege().getId()))
                .toList();

        if (!toDelete.isEmpty()) {
            toDelete.forEach(group.getGroupPrivileges()::remove);
        }

        for (GroupPrivilege gp : toSave) {
            group.getGroupPrivileges().add(gp);
        }

        group.syncAuthorities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PrivilegeActionsVO> privileges(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return groupPrivilegeRepository.findAllByGroupId(id)
                .stream().map(PrivilegeActionsVO::from)
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
