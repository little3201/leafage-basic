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

package io.leafage.basic.assets.service;

import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.domain.PostStatistics;
import reactor.core.publisher.Mono;

/**
 * statistics service
 *
 * @author liwenqiang 2021-05-19 10:54
 **/
public interface PostStatisticsService {

    /**
     * 记录统计量
     *
     * @param postId              帖子ID
     * @param statisticsFieldEnum 统计枚举
     * @return 浏览量
     */
    Mono<PostStatistics> increase(Long postId, StatisticsFieldEnum statisticsFieldEnum);

}
