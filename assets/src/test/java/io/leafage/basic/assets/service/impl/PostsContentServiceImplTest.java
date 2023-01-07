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
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * posts content service test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class PostsContentServiceImplTest {

    @Mock
    private PostsContentRepository postsContentRepository;

    @InjectMocks
    private PostsContentServiceImpl postsContentService;

    @Test
    void create() {
        given(this.postsContentRepository.insert(Mockito.any(PostsContent.class)))
                .willReturn(Mono.just(Mockito.mock(PostsContent.class)));

        StepVerifier.create(this.postsContentService.create(Mockito.mock(PostsContent.class)))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        PostsContent postsContent = new PostsContent();
        postsContent.setId(new ObjectId());
        postsContent.setContent("test");
        given(this.postsContentRepository.getByPostsIdAndEnabledTrue(Mockito.any(ObjectId.class)))
                .willReturn(Mono.just(postsContent));

        given(this.postsContentRepository.save(Mockito.any(PostsContent.class)))
                .willReturn(Mono.just(Mockito.mock(PostsContent.class)));

        StepVerifier.create(this.postsContentService.modify(new ObjectId(), postsContent)).expectNextCount(1).verifyComplete();
    }
}