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

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.repository.PostContentRepository;
import io.leafage.basic.assets.service.PostContentService;
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
public class PostContentServiceImpl implements PostContentService {

    private final PostContentRepository postContentRepository;

    public PostContentServiceImpl(PostContentRepository postContentRepository) {
        this.postContentRepository = postContentRepository;
    }

    @Override
    public Mono<PostContent> create(PostContent postContent) {
        return postContentRepository.save(postContent);
    }

    @Override
    public Mono<PostContent> modify(Long postId, PostContent postContent) {
        Assert.notNull(postId, "postId must not be null.");
        return this.fetchByPostId(postId).doOnNext(details -> BeanUtils.copyProperties(postContent, details))
                .flatMap(postContentRepository::save);
    }

    @Override
    public Mono<PostContent> fetchByPostId(Long postsId) {
        Assert.notNull(postsId, "postsId must not be null.");
        return postContentRepository.getByPostIdAndEnabledTrue(postsId);
    }

}
