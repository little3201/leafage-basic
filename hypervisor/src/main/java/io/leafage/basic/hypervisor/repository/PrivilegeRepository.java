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
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * privilege repository.
 *
 * @author wq li
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    /**
     * <p>findAllBySuperiorIdIsNull.</p>
     *
     * @param pageable a {@link org.springframework.data.domain.Pageable} object
     * @return a {@link org.springframework.data.domain.Page} object
     */
    Page<Privilege> findAllBySuperiorIdIsNull(Pageable pageable);

    /**
     * <p>findBySuperiorId.</p>
     *
     * @param superiorId a {@link java.lang.Long} object
     * @return a {@link java.util.List} object
     */
    List<Privilege> findBySuperiorId(Long superiorId);

    /**
     * <p>countBySuperiorId.</p>
     *
     * @param superiorId a {@link java.lang.Long} object
     * @return a long
     */
    long countBySuperiorId(Long superiorId);
}
