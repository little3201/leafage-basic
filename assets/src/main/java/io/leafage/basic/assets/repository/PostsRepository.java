/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 帖子信息repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface PostsRepository extends ReactiveMongoRepository<Posts, String> {

    /**
     * 分页查询帖子
     *
     * @param pageable 分页参数
     * @return 有效帖子
     */
    Flux<Posts> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<Posts> getByCodeAndEnabledTrue(String code);

    /**
     * 统计关联帖子
     *
     * @param categoryId 分类ID
     * @return 用户数
     */
    Mono<Long> countByCategoryIdAndEnabledTrue(String categoryId);
}
