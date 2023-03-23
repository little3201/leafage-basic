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
import io.leafage.basic.assets.domain.Statistics;
import io.leafage.basic.assets.dto.StatisticsDTO;
import io.leafage.basic.assets.vo.StatisticsVO;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

import java.time.LocalDate;

/**
 * statistics service
 *
 * @author liwenqiang 2021-05-19 10:54
 **/
public interface StatisticsService extends ReactiveBasicService<StatisticsDTO, StatisticsVO, String> {

    /**
     * 记录统计量
     *
     * @param today           当日
     * @param statisticsField 统计记录属性名（viewed, likes, comments, downloads）
     * @return 浏览量
     */
    Mono<Statistics> increase(LocalDate today, StatisticsFieldEnum statisticsField);

}
