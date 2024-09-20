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
import io.leafage.basic.assets.service.PostStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * statistics service impl
 *
 * @author wq li
 */
@Service
public class PostStatisticsServiceImpl implements PostStatisticsService {

    private final StatisticsRepository statisticsRepository;

    /**
     * <p>Constructor for PostStatisticsServiceImpl.</p>
     *
     * @param statisticsRepository a {@link io.leafage.basic.assets.repository.StatisticsRepository} object
     */
    public PostStatisticsServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<PostStatistics> increase(Long postId, StatisticsEnum statisticsEnum) {
        Assert.notNull(postId, "postId must not be null.");
        return statisticsRepository.getByPostId(postId).flatMap(postStatistics -> {
            switch (statisticsEnum) {
                case LIKES -> postStatistics.setLikes(postStatistics.getLikes() + 1);
                case COMMENTS -> postStatistics.setComments(postStatistics.getComments() + 1);
                case VIEWED -> postStatistics.setViewed(postStatistics.getViewed() + 1);
            }
            return statisticsRepository.save(postStatistics);
        });
    }

}
