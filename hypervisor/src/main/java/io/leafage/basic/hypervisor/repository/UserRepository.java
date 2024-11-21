/*
 * Copyright (c) 2024.  little3201.
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
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * user repository.
 *
 * @author wq li
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * find user by username
     *
     * @param username username
     * @return Record data
     */
    Optional<User> findByUsername(String username);

    /**
     * 是否存在
     *
     * @param username 用户名
     * @return true-存在，false-否
     */
    boolean existsByUsername(String username);

    /**
     * 是否存在
     *
     * @param username 用户名
     * @param id       the record's id.
     * @return true-存在，false-否
     */
    boolean existsByUsernameAndIdNot(String username, Long id);

    /**
     * Toggles the enabled status of a record by its ID.
     *
     * @param id The ID of the record.
     * @return true if the update was successful, false otherwise.
     */
    @Modifying
    @Query("UPDATE User t SET t.enabled = CASE WHEN t.enabled = true THEN false ELSE true END WHERE t.id = :id")
    boolean updateEnabledById(Long id);
}
