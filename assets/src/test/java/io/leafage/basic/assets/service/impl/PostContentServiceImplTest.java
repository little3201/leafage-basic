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
import org.junit.jupiter.api.BeforeEach;
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
class PostContentServiceImplTest {

    @Mock
    private PostContentRepository postContentRepository;

    @InjectMocks
    private PostContentServiceImpl postsContentService;

    private PostContent postContent;

    @BeforeEach
    void init() {
        postContent = new PostContent();
        postContent.setId(1L);
        postContent.setContext("test");
        postContent.setCatalog("## First");
    }

    @Test
    void create() {
        given(this.postContentRepository.save(Mockito.any(PostContent.class)))
                .willReturn(Mono.just(Mockito.mock(PostContent.class)));

        StepVerifier.create(this.postsContentService.create(Mockito.mock(PostContent.class)))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.postContentRepository.getByPostIdAndEnabledTrue(Mockito.any(Long.class)))
                .willReturn(Mono.just(postContent));

        given(this.postContentRepository.save(Mockito.any(PostContent.class)))
                .willReturn(Mono.just(Mockito.mock(PostContent.class)));

        StepVerifier.create(this.postsContentService.modify(Mockito.anyLong(), postContent)).expectNextCount(1).verifyComplete();
    }
}