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

package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Post;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * posts repository
 *
 * @author liwenqiang 2018-12-20 09:51
 **/
@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, ObjectId> {

    /**
     * 查询
     *
     * @return 有效帖子
     */
    Flux<Post> findByEnabledTrue();

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效帖子
     */
    Flux<Post> findByEnabledTrue(Pageable pageable);

    /**
     * 根据分类分页查询
     *
     * @param categoryId 分类ID
     * @param pageable   分页参数
     * @return 有效帖子
     */
    Flux<Post> findByCategoryIdAndEnabledTrue(ObjectId categoryId, Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<Post> getByCodeAndEnabledTrue(String code);

    /**
     * 关联统计
     *
     * @param categoryId 分类ID
     * @return 帖子数
     */
    Mono<Long> countByCategoryIdAndEnabledTrue(ObjectId categoryId);

    /**
     * 查询下一相邻的记录
     *
     * @param id 主键
     * @return 帖子信息
     */
    Mono<Post> findFirstByIdGreaterThanAndEnabledTrue(ObjectId id);

    /**
     * 查询上一相邻的记录
     *
     * @param id       主键
     * @param pageable 分页对象
     * @return 帖子信息
     */
    Flux<Post> findByIdLessThanAndEnabledTrue(ObjectId id, Pageable pageable);

    /**
     * 关键词查询
     *
     * @param keyword 关键词
     * @return 匹配结果
     */
    Flux<Post> findAllBy(String keyword, TextCriteria criteria);

    /**
     * 是否已存在
     *
     * @param title 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByTitle(String title);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
