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
package top.leafage.hypervisor.audits.service.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.audits.service.PatrolService;
import top.leafage.hypervisor.system.domain.Role;
import top.leafage.hypervisor.system.domain.User;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.repository.UserRepository;

import java.util.Optional;

/**
 * Patrol service impl.
 *
 * @author wq li
 */
@OperationLog("users")
@Service
public class PatrolServiceImpl implements PatrolService {

    private final UserRepository userRepository;

    /**
     * Constructor for PatrolServiceImpl.
     *
     * @param userRepository a {@link UserRepository} object
     */
    public PatrolServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<UserVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<User> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            Join<User, Role> roleJoin =
                    root.join("roles", JoinType.INNER);

            Predicate rolePredicate =
                    cb.equal(roleJoin.get("code"), "AUDITOR");

            return cb.and(basePredicate, rolePredicate);
        };

        return userRepository.findAll(spec, pageable)
                .map(UserVO::from);
    }

}
