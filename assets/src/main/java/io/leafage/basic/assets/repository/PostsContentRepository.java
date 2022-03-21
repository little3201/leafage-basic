/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.PostsContent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * posts content repository
 *
 * @author liwenqiang 2020/2/26 18:29
 **/
@Repository
public interface PostsContentRepository extends ReactiveMongoRepository<PostsContent, ObjectId> {

    /**
     * 查询信息
     *
     * @param postsId 帖子id
     * @return 内容
     */
    Mono<PostsContent> getByPostsIdAndEnabledTrue(ObjectId postsId);

}
