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

package io.leafage.basic.assets.service;

import io.leafage.basic.assets.document.PostsContent;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

/**
 * posts service
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
