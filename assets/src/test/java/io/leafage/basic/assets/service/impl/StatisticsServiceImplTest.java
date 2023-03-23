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

import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.domain.Statistics;
import io.leafage.basic.assets.dto.StatisticsDTO;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

/**
 * statistics service test
 *
 * @author liwenqiang 2021/5/22 20:50
 */
@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    private Statistics statistics;

    @BeforeEach
    void init() {
        statistics = new Statistics();
        statistics.setPostId(1L);
    }

    @Test
    void create() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.statisticsRepository.save(Mockito.any(Statistics.class)))
                .willReturn(Mono.just(statistics));

        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Post.class)));

        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setPost("21213G0J2");
        statisticsDTO.setLikes(23);
        statisticsDTO.setComments(23);
        statisticsDTO.setViewed(322);
        statisticsDTO.setModifyTime(LocalDateTime.now());

        StepVerifier.create(statisticsService.create(statisticsDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void increase() {
        given(this.statisticsRepository.getByModifyTime(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.increase(LocalDate.now(), StatisticsFieldEnum.VIEWED))
                .expectNextCount(1).verifyComplete();
    }
}