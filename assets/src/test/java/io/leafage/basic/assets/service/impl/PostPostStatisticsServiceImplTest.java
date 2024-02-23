/*
 *  Copyright 2018-2024 little3201.
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

import io.leafage.basic.assets.constants.StatisticsEnum;
import io.leafage.basic.assets.domain.PostStatistics;
import io.leafage.basic.assets.repository.StatisticsRepository;
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
 * statistics service test
 *
 * @author liwenqiang 2021-05-22 20:50
 */
@ExtendWith(MockitoExtension.class)
class PostPostStatisticsServiceImplTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private PostStatisticsServiceImpl statisticsService;


    @Test
    void increase_view() {
        given(this.statisticsRepository.getByPostId(Mockito.anyLong()))
                .willReturn(Mono.just(Mockito.mock(PostStatistics.class)));

        given(this.statisticsRepository.save(Mockito.any(PostStatistics.class))).willReturn(Mono.just(Mockito.mock(PostStatistics.class)));

        StepVerifier.create(statisticsService.increase(Mockito.anyLong(), StatisticsEnum.VIEWED))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void increase_likes() {
        given(this.statisticsRepository.getByPostId(Mockito.anyLong()))
                .willReturn(Mono.just(Mockito.mock(PostStatistics.class)));

        given(this.statisticsRepository.save(Mockito.any(PostStatistics.class))).willReturn(Mono.just(Mockito.mock(PostStatistics.class)));

        StepVerifier.create(statisticsService.increase(Mockito.anyLong(), StatisticsEnum.LIKES))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void increase_comments() {
        given(this.statisticsRepository.getByPostId(Mockito.anyLong()))
                .willReturn(Mono.just(Mockito.mock(PostStatistics.class)));

        given(this.statisticsRepository.save(Mockito.any(PostStatistics.class))).willReturn(Mono.just(Mockito.mock(PostStatistics.class)));

        StepVerifier.create(statisticsService.increase(Mockito.anyLong(), StatisticsEnum.COMMENTS))
                .expectNextCount(1).verifyComplete();
    }
}