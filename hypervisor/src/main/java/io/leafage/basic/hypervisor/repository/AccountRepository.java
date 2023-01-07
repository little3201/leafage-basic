/*
 *  Copyright 2018-2022 the original author or authors.
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

import io.leafage.basic.hypervisor.document.Account;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * account repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, ObjectId> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效记录
     */
    Flux<Account> findByEnabledTrue(Pageable pageable);

    /**
     * 根据账号查询
     *
     * @param username 账号
     * @return 账户信息
     */
    Mono<Account> getByUsernameAndEnabledTrue(String username);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
