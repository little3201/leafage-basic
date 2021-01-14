/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.PostsContent;

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
     * 根据文章ID修改信息
     *
     * @param articleId    文章ID
     * @param postsContent 信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<PostsContent> modify(String articleId, PostsContent postsContent);

    /**
     * 根据文章ID查询
     *
     * @param articleId 文章ID
     * @return 返回查询到的信息，否则返回empty
     */
    Mono<PostsContent> fetchByPostsId(String articleId);
}