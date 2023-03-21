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

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.document.Post;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.dto.StatisticsDTO;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    void create() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Post.class)));

        Statistics statistics = new Statistics();
        statistics.setPostId(new ObjectId());
        given(this.statisticsRepository.insert(Mockito.any(Statistics.class)))
                .willReturn(Mono.just(statistics));

        given(this.postRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Post.class)));

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
        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("date").is(LocalDate.now())),
                new Update().inc(StatisticsFieldEnum.VIEWED.value, 1), Statistics.class))
                .willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        given(this.statisticsRepository.getByModifyTime(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.increase(LocalDate.now(), StatisticsFieldEnum.VIEWED))
                .expectNextCount(1).verifyComplete();
    }
}