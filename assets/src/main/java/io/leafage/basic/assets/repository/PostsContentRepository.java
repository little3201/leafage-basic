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

import io.leafage.basic.assets.document.PostsContent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * posts content repository
 *
 * @author liwenqiang 2020-02-26 18:29
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
