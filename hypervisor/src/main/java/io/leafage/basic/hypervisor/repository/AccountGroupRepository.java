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

import io.leafage.basic.hypervisor.document.AccountGroup;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * account group repository
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface AccountGroupRepository extends ReactiveCrudRepository<AccountGroup, ObjectId> {

    /**
     * 统计关联用户
     *
     * @param groupId 组ID
     * @return 用户数
     */
    Mono<Long> countByGroupIdAndEnabledTrue(ObjectId groupId);

    /**
     * 根据分组查用户
     *
     * @param groupId 分组主键
     * @return 关联数据集
     */
    Flux<AccountGroup> findByGroupIdAndEnabledTrue(ObjectId groupId);

    /**
     * 根据用户查分组
     *
     * @param accountId 账号主键
     * @return 关联数据集
     */
    Flux<AccountGroup> findByAccountIdAndEnabledTrue(ObjectId accountId);
}
