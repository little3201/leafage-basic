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

import io.leafage.basic.hypervisor.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * group repository.
 *
 * @author wq li 2018/12/20 9:52
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * <p>findAllBySuperiorId.</p>
     *
     * @param superiorId a {@link java.lang.Long} object
     * @param pageable   a {@link org.springframework.data.domain.Pageable} object
     * @return a {@link org.springframework.data.domain.Page} object
     */
    Page<Group> findAllBySuperiorId(Long superiorId, Pageable pageable);
}
