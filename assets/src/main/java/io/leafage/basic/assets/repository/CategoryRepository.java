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

package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Category;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * category repository
 *
 * @author liwenqiang 2020-02-13 22:01
 **/
@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category, ObjectId> {

    /**
     * 查询所有类别
     *
     * @return 有效类别
     */
    Flux<Category> findByEnabledTrue();

    /**
     * 分页查询类别
     *
     * @param pageable 分页参数
     * @return 有效类别
     */
    Flux<Category> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 类别信息
     */
    Mono<Category> getByCodeAndEnabledTrue(String code);

    /**
     * 是否已存在
     *
     * @param alias 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByName(String alias);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
