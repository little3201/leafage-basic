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
import org.springframework.util.StringUtils;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.system.domain.*;
import top.leafage.hypervisor.system.domain.dto.RoleDTO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.SimplePrivilegeVO;
import top.leafage.hypervisor.system.repository.*;
import top.leafage.hypervisor.system.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;
import static top.leafage.hypervisor.constants.GlobalConstant._MUST_NOT_BE_NULL;

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
    public void addPrivilege(Long id, Long privilegeId, String action) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Assert.notNull(privilegeId, String.format(_MUST_NOT_BE_NULL, "privilegeId"));

        Privilege priv = privilegeRepository.findById(privilegeId).orElseThrow();
        if (StringUtils.hasText(action) && !priv.getActions().contains(action)) {
            throw new IllegalArgumentException("无效的 action");
        }
        Set<String> actions = StringUtils.hasText(action) ? Set.of(action) : Set.of();

        Optional<RolePrivilege> existing = rolePrivilegeRepository
                .findByRoleIdAndPrivilegeId(id, privilegeId);

        Role role = roleRepository.findById(id).orElseThrow();
        if (existing.isPresent()) {
            // 已存在时只追加指定 action，避免覆盖同一 privilege 下的其他 action。
            existing.get().addActions(actions);
            rolePrivilegeRepository.save(existing.get());
        } else {
            // 不存在 → 新增
            role.addPrivilege(priv, actions);
            roleRepository.save(role);
        }
        syncAllGroupsContainingRole(role);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<SimplePrivilegeVO> privileges(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return rolePrivilegeRepository.findAllByRoleId(id)
                .stream().map(SimplePrivilegeVO::from)
                .toList();
    }

    @Transactional
    @Override
    public void removePrivilege(Long id, Long privilegeId, String action) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Assert.notNull(privilegeId, String.format(_MUST_NOT_BE_NULL, "privilegeId"));

        Role role = roleRepository.findById(id).orElseThrow();
        Privilege priv = privilegeRepository.findById(privilegeId).orElseThrow();
        if (StringUtils.hasText(action)) {
            if (!priv.getActions().contains(action)) {
                throw new IllegalArgumentException("无效的 action");
            }
            role.removePrivilegeAction(priv, action);
        } else {
            role.removePrivilege(priv);
        }

        roleRepository.save(role);

        syncAllGroupsContainingRole(role);

        syncAllUsersContainingRole(role);
    }

    /**
     * 更新关联了角色的组权限
     *
     * @param role 角色
     */
    private void syncAllGroupsContainingRole(Role role) {
        List<Group> groups = groupRepository.findDisctinctByRolesContaining(role);
        for (Group group : groups) {
            group.syncAuthorities();
            groupRepository.save(group);
        }
    }

    /**
     * 更新关联了角色的用户权限
     *
     * @param role 角色
     */
    private void syncAllUsersContainingRole(Role role) {
        List<User> groups = userRepository.findDisctinctByRolesContaining(role);
        for (User user : groups) {
            user.syncAuthorities();
            userRepository.save(user);
        }
    }
}
