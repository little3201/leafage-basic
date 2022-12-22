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

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.repository.PostsContentRepository;
import io.leafage.basic.assets.service.PostsContentService;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * posts content service impl
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Service
public class PostsContentServiceImpl implements PostsContentService {

    private final PostsContentRepository postsContentRepository;

    public PostsContentServiceImpl(PostsContentRepository postsContentRepository) {
        this.postsContentRepository = postsContentRepository;
    }

    @Override
    public Mono<PostsContent> create(PostsContent postsContent) {
        return postsContentRepository.insert(postsContent);
    }

    @Override
    public Mono<PostsContent> modify(ObjectId postsId, PostsContent postsContent) {
        Assert.notNull(postsId, "postsId must not be null.");
        return this.fetchByPostsId(postsId).doOnNext(details -> BeanUtils.copyProperties(postsContent, details))
                .flatMap(postsContentRepository::save);
    }

    @Override
    public Mono<PostsContent> fetchByPostsId(ObjectId postsId) {
        Assert.notNull(postsId, "postsId must not be null.");
        return postsContentRepository.getByPostsIdAndEnabledTrue(postsId);
    }

}
