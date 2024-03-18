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

import io.leafage.basic.assets.domain.PostStatistics;
import io.leafage.basic.assets.dto.PostStatisticsDTO;
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import io.leafage.basic.assets.service.PostStatisticsService;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * statistics service impl.
 *
 * @author wq li 2021/09/29 15:30
 **/
@Service
public class PostStatisticsServiceImpl implements PostStatisticsService {

    private final PostStatisticsRepository postStatisticsRepository;

    public PostStatisticsServiceImpl(PostStatisticsRepository postStatisticsRepository) {
        this.postStatisticsRepository = postStatisticsRepository;
    }

    @Override
    public Page<PostStatisticsVO> retrieve(int page, int size) {
        return postStatisticsRepository.findAll(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public PostStatisticsVO create(PostStatisticsDTO dto) {
        PostStatistics postStatistics = new PostStatistics();
        BeanCopier copier = BeanCopier.create(PostStatisticsDTO.class, PostStatistics.class, false);
        copier.copy(dto, postStatistics, null);

        postStatistics = postStatisticsRepository.saveAndFlush(postStatistics);
        return this.convertOuter(postStatistics);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param postStatistics 信息
     * @return 输出转换后的vo对象
     */
    private PostStatisticsVO convertOuter(PostStatistics postStatistics) {
        PostStatisticsVO vo = new PostStatisticsVO();
        BeanCopier copier = BeanCopier.create(PostStatistics.class, PostStatisticsVO.class, false);
        copier.copy(postStatistics, vo, null);
        return vo;
    }

}
