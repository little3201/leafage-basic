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

package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.domain.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * region repository.
 *
 * @author wq li
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Page<Region> findBySuperiorIdIsNull(Pageable pageable);

    /**
     * <p>existsByName.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a boolean
     */
    boolean existsByName(String name);

    /**
     * <p>findBySuperiorId.</p>
     *
     * @param id a {@link java.lang.Long} object
     * @return a {@link java.util.List} object
     */
    List<Region> findBySuperiorId(Long id);

}
