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

import io.leafage.basic.assets.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * comment repository
 *
 * @author liwenqiang 2020-02-13 22:01
 **/
@Repository
public interface CommentRepository extends R2dbcRepository<Comment, Long> {


    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效评论
     */
    Flux<Comment> findByEnabledTrue(Pageable pageable);

    /**
     * 根据帖子ID查询
     *
     * @param postId 帖子ID
     * @return 关联的数据
     */
    Flux<Comment> findByPostIdAndReplierIsNullAndEnabledTrue(Long postId);

    /**
     * 统计回复
     *
     * @param replier 关联代码
     * @return 关联的数据记录数
     */
    Mono<Long> countByReplierAndEnabledTrue(String replier);

    /**
     * 查询回复
     *
     * @param replier 关联代码
     * @return 关联的数据
     */
    Flux<Comment> findByReplierAndEnabledTrue(String replier);

    /**
     * 根据code查询信息
     *
     * @param code 代码
     * @return 评论信息
     */
    Mono<Comment> getByCodeAndEnabledTrue(String code);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
