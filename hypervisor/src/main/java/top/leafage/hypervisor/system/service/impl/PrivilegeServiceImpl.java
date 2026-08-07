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
import top.leafage.common.data.core.domain.TreeNode;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.system.domain.Privilege;
import top.leafage.hypervisor.system.domain.dto.PrivilegeDTO;
import top.leafage.hypervisor.system.domain.vo.PrivilegeVO;
import top.leafage.hypervisor.system.repository.PrivilegeRepository;
import top.leafage.hypervisor.system.service.PrivilegeService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static top.leafage.common.data.core.converter.ModelToTreeNodeConverter.toTree;
import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;
import static top.leafage.hypervisor.constants.GlobalConstant._MUST_NOT_BE_NULL;

/**
 * Privilege service impl.
 *
 * @author wq li
 */
@OperationLog("privileges")
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private static final Set<String> META_FIELDS = Set.of("path", "redirect", "component", "actions");

    private static final BeanCopier copier = BeanCopier.create(PrivilegeDTO.class, Privilege.class, false);

    private final PrivilegeRepository privilegeRepository;

    /**
     * Constructor for PrivilegeServiceImpl.
     *
     * @param privilegeRepository a {@link PrivilegeRepository} object
     */
    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PrivilegeVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Privilege> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            // 添加superiorId的条件
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            return cb.and(basePredicate, cb.isNull(root.get("superiorId")));
        };

        Page<Privilege> entityPage = privilegeRepository.findAll(spec, pageable);
        if (entityPage.isEmpty()) {
            return Page.empty(pageable);
        }
        Set<Long> ids = entityPage.getContent().stream()
                .map(Privilege::getId)
                .collect(Collectors.toSet());

        List<Object[]> countResults = privilegeRepository.countBySuperiorIdsGrouped(ids);
        Map<Long, Long> countMap = countResults.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // superiorId
                        row -> (Long) row[1]   // count
                ));

        return entityPage.map(entity -> {
            long count = countMap.getOrDefault(entity.getId(), 0L);
            return PrivilegeVO.from(entity, count);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TreeNode<@NonNull Long>> tree() {
        Set<Long> privilegeIds = new LinkedHashSet<>();
        privilegeIds.addAll(privilegeRepository.findGroupPrivilegeIds());
        privilegeIds.addAll(privilegeRepository.findGroupRolePrivilegeIds());
        privilegeIds.addAll(privilegeRepository.findRolePrivilegeIds());
        if (CollectionUtils.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }

        Map<Long, Privilege> privilegeMap = privilegeRepository.findAllById(privilegeIds).stream()
                .filter(Privilege::isEnabled)
                .collect(Collectors.toMap(Privilege::getId, Function.identity()));

        expandPrivileges(privilegeMap);

        List<Privilege> allPrivileges = new ArrayList<>(privilegeMap.values());
        return toTree(allPrivileges, META_FIELDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PrivilegeVO> subset(Long superiorId) {
        Assert.notNull(superiorId, String.format(_MUST_NOT_BE_NULL, "superiorId"));

        List<Privilege> list = privilegeRepository.findAllBySuperiorId(superiorId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Set<Long> ids = list.stream()
                .map(Privilege::getId)
                .collect(Collectors.toSet());

        List<Object[]> countResults = privilegeRepository.countBySuperiorIdsGrouped(ids);
        Map<Long, Long> countMap = countResults.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // superiorId
                        row -> (Long) row[1]   // count
                ));

        return list.stream()
                .map(entity -> {
                    long count = countMap.getOrDefault(entity.getId(), 0L);
                    return PrivilegeVO.from(entity, count);
                })
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrivilegeVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return privilegeRepository.findById(id)
                .map(PrivilegeVO::from)
                .orElseThrow(() -> new EntityNotFoundException("privilege log not found: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!privilegeRepository.existsById(id)) {
            throw new EntityNotFoundException("privilege not found: " + id);
        }
        return privilegeRepository.enableById(id) > 0;
    }

    @Transactional
    @Override
    public boolean disable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!privilegeRepository.existsById(id)) {
            throw new EntityNotFoundException("privilege not found: " + id);
        }
        return privilegeRepository.disableById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PrivilegeVO modify(Long id, PrivilegeDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Privilege existing = privilegeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("privilege not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                privilegeRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        copier.copy(dto, existing, null);
        Privilege entity = privilegeRepository.save(existing);
        return PrivilegeVO.from(entity);
    }

    private Set<Long> collectMissingSuperiorIds(Map<Long, Privilege> privilegeMap) {
        return privilegeMap.values().stream()
                .map(Privilege::getSuperiorId)
                .filter(Objects::nonNull)
                .filter(id -> !privilegeMap.containsKey(id))
                .collect(Collectors.toSet());
    }

    private void expandPrivileges(Map<Long, Privilege> privilegeMap) {
        final int MAX_ITERATIONS = 8;
        int iterations = 0;
        Set<Long> toLoad;
        do {
            toLoad = collectMissingSuperiorIds(privilegeMap);
            if (toLoad.isEmpty()) {
                break;
            }

            if (++iterations > MAX_ITERATIONS) {
                break;
            }

            privilegeRepository.findAllById(toLoad).stream()
                    .filter(Privilege::isEnabled)
                    .forEach(sup -> privilegeMap.putIfAbsent(sup.getId(), sup));

        } while (true);
    }

}
