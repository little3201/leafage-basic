/*
 *  Copyright 2018-2023 the original author or authors.
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
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * user repository
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    /**
     * 根据账号查
     *
     * @param username 账号
     * @return 用户信息
     */
    Mono<User> getByUsername(String username);

    /**
     * 是否已存在
     *
     * @param username 账号
     * @param phone    电话
     * @param email    邮箱
     * @return true-是，false-否
     */
    Mono<Boolean> existsByUsernameOrPhoneOrEmail(String username, String phone, String email);

    /**
     * 删除
     *
     * @param username 账号
     * @return 用户信息
     */
    Mono<Void> deleteByUsername(String username);
}
