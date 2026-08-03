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
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.system.domain.*;
import top.leafage.hypervisor.system.domain.dto.PrivilegeActionsDTO;
import top.leafage.hypervisor.system.domain.dto.RoleDTO;
import top.leafage.hypervisor.system.domain.vo.PrivilegeActionsVO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.repository.*;
import top.leafage.hypervisor.system.service.RoleService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;

/**
 * Role service impl.
 *
 * @author wq li
 */
@OperationLog("roles")
@Service
public class RoleServiceImpl implements RoleService {

    private static final BeanCopier copier = BeanCopier.create(RoleDTO.class, Role.class, false);

    private final RoleRepository roleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final PrivilegeRepository privilegeRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for RoleServiceImpl.
     *
     * @param roleRepository          a {@link RoleRepository} object
     * @param rolePrivilegeRepository a {@link RolePrivilegeRepository} object
     * @param privilegeRepository     a {@link PrivilegeRepository} object
     * @param groupRepository         a {@link GroupRepository} object
     * @param userRepository          a {@link UserRepository} object
     */
    public RoleServiceImpl(RoleRepository roleRepository, RolePrivilegeRepository rolePrivilegeRepository,
                           PrivilegeRepository privilegeRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
        this.privilegeRepository = privilegeRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<RoleVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Role> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);

        return roleRepository.findAll(spec, pageable)
                .map(RoleVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return roleRepository.findById(id)
                .map(RoleVO::from)
                .orElseThrow(() -> new EntityNotFoundException("role not found: " + id));
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("role not found: " + id);
        }
        return roleRepository.enableById(id) > 0;
    }

    @Transactional
    @Override
    public boolean disable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("role not found: " + id);
        }
        return roleRepository.disableById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public RoleVO create(RoleDTO dto) {
        if (roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        Role entity = roleRepository.save(RoleDTO.toEntity(dto));
        return RoleVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public RoleVO modify(Long id, RoleDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("role not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        copier.copy(dto, existing, null);
        Role entity = roleRepository.save(existing);
        return RoleVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("role not found: " + id);
        }
        roleRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void authorize(Long id, Collection<PrivilegeActionsDTO> dtos) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Role role = roleRepository.findById(id).orElseThrow();

        Set<Long> privilegeIds = dtos.stream()
                .map(PrivilegeActionsDTO::getPrivilegeId)
                .collect(Collectors.toSet());

        // 批量查询 privilege
        Map<Long, Privilege> privileges = privilegeRepository.findAllById(privilegeIds)
                .stream()
                .collect(Collectors.toMap(Privilege::getId, Function.identity()));
        if (privileges.size() != privilegeIds.size()) {
            throw new IllegalArgumentException("Invalid privilege");
        }

        // 查出当前 Role 已有的全部授权
        List<RolePrivilege> existingList = rolePrivilegeRepository.findAllByRoleId(id);
        Map<Long, RolePrivilege> existing = existingList.stream()
                .collect(Collectors.toMap(rp -> rp.getPrivilege().getId(), Function.identity()));

        List<RolePrivilege> toSave = new ArrayList<>();
        Set<Long> toKeep = new HashSet<>();

        for (PrivilegeActionsDTO dto : dtos) {
            Privilege privilege = privileges.get(dto.getPrivilegeId());
            Set<String> actions = Optional.ofNullable(dto.getActions()).orElse(Collections.emptySet());

            if (!privilege.getActions().containsAll(actions)) {
                throw new IllegalArgumentException("Invalid action: " + actions);
            }

            RolePrivilege rolePrivilege = existing.get(dto.getPrivilegeId());
            if (rolePrivilege != null) {
                rolePrivilege.updateActions(actions);
            } else {
                rolePrivilege = new RolePrivilege(role, privilege, actions);
            }
            toSave.add(rolePrivilege);
            toKeep.add(dto.getPrivilegeId());
        }

        // 需要删除的 = 已存在但不在本次传入列表中的
        List<RolePrivilege> toDelete = existingList.stream()
                .filter(rp -> !toKeep.contains(rp.getPrivilege().getId()))
                .toList();
        if (!toDelete.isEmpty()) {
            toDelete.forEach(role.getRolePrivileges()::remove);
        }

        for (RolePrivilege rp : toSave) {
            role.getRolePrivileges().add(rp);
        }

        syncGroupsRole(role);
        syncUsersRole(role);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<PrivilegeActionsVO> privileges(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return rolePrivilegeRepository.findAllByRoleId(id)
                .stream().map(PrivilegeActionsVO::from)
                .toList();
    }

    /**
     * 更新关联了角色的组权限
     *
     * @param role 角色
     */
    private void syncGroupsRole(Role role) {
        List<Group> groups = groupRepository.findDisctinctByRolesContaining(role);
        groups.forEach(Group::syncAuthorities);
        groupRepository.saveAll(groups);
    }

    /**
     * 更新关联了角色的用户权限
     *
     * @param role 角色
     */
    private void syncUsersRole(Role role) {
        List<User> users = userRepository.findDisctinctByRolesContaining(role);
        users.forEach(User::syncAuthorities);
        userRepository.saveAll(users);
    }
}
