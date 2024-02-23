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

import io.leafage.basic.hypervisor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * user repository.
 *
 * @author wq li 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return user
     */
    User getByUsername(String username);

    /**
     * 是否存在
     *
     * @param username 用户名
     * @return true-存在，false-否
     */
    boolean existsByUsername(String username);
}
