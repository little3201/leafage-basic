/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.PostsContent;

/**
 * 内容信息repository
 *
 * @author liwenqiang 2020/2/26 18:29
 **/
@Repository
public interface PostsContentRepository extends ReactiveMongoRepository<PostsContent, String> {

    /**
     * 查询信息
     *
     * @param postsId 帖子id
     * @return 内容
     */
    Mono<PostsContent> findByPostsIdAndEnabledTrue(String postsId);

}
