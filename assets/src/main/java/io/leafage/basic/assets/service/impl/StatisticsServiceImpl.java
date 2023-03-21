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
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.dto.StatisticsDTO;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * statistics service impl
 *
 * @author liwenqiang 2021-05-19 10:54
 **/
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final String DATE = "date";

    private final StatisticsRepository statisticsRepository;
    private final PostRepository postRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public StatisticsServiceImpl(StatisticsRepository statisticsRepository, PostRepository postRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.statisticsRepository = statisticsRepository;
        this.postRepository = postRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<StatisticsVO> create(StatisticsDTO statisticsDTO) {
        Statistics statistics = new Statistics();
        BeanUtils.copyProperties(statisticsDTO, statistics);

        return postRepository.getByCodeAndEnabledTrue(statisticsDTO.getPost()).map(posts -> {
            statistics.setPostId(posts.getId());
            return statistics;
        }).flatMap(statisticsRepository::insert).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Statistics> increase(LocalDate today, StatisticsFieldEnum statisticsFieldEnum) {
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where(DATE).is(today)),
                        new Update().inc(statisticsFieldEnum.value, 1), Statistics.class)
                .flatMap(updateResult -> statisticsRepository.getByModifyTime(today));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param statistics 信息
     * @return 输出转换后的vo对象
     */
    private Mono<StatisticsVO> convertOuter(Statistics statistics) {
        StatisticsVO outer = new StatisticsVO();
        BeanUtils.copyProperties(statistics, outer);
        return postRepository.findById(statistics.getPostId()).map(posts -> {
            outer.setPost(posts.getCode());
            return outer;
        });
    }

}
