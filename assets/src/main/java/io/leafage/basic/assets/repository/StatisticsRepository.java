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

package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.domain.PostStatistics;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * statistics repository
 *
 * @author liwenqiang 2020-02-13 22:01
 **/
@Repository
public interface StatisticsRepository extends R2dbcRepository<PostStatistics, Long> {

    /**
     * 查询帖子数据
     *
     * @param postId 帖子ID
     * @return 统计数据
     */
    Mono<PostStatistics> getByPostId(Long postId);
}
