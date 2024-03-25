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
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * statistics 接口测试
 *
 * @author wq li 2021/12/7 17:55
 **/
@ExtendWith(MockitoExtension.class)
class PostStatisticsServiceImplTest {

    @Mock
    private PostStatisticsRepository postStatisticsRepository;

    @InjectMocks
    private PostStatisticsServiceImpl postStatisticsService;


    @Test
    void increase() {
        given(postStatisticsRepository.getByPostId(Mockito.anyLong())).willReturn(Mockito.mock(PostStatistics.class));

        given(postStatisticsRepository.save(Mockito.any(PostStatistics.class))).willReturn(Mockito.mock(PostStatistics.class));

        PostStatistics postStatistics = postStatisticsService.increase(Mockito.anyLong(), StatisticsEnum.COMMENTS);

        verify(postStatisticsRepository, times(1)).save(Mockito.any(PostStatistics.class));
        Assertions.assertNotNull(postStatistics);
    }

}