/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package io.leafage.basic.assets.service;

import io.leafage.basic.assets.document.PostsContent;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

/**
 * 内容service接口
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface PostsContentService {

    /**
     * 新增信息
     *
     * @param postsContent 内容信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<PostsContent> create(PostsContent postsContent);

    /**
     * 根据帖子ID修改信息
     *
     * @param postsId      帖子ID
     * @param postsContent 信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<PostsContent> modify(ObjectId postsId, PostsContent postsContent);

    /**
     * 根据帖子ID查询
     *
     * @param postsId 帖子ID
     * @return 返回查询到的信息，否则返回empty
     */
    Mono<PostsContent> fetchByPostsId(ObjectId postsId);
}
