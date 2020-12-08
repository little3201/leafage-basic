/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Posts;

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
    Mono<Posts> findByCodeAndEnabledTrue(String code);
}
