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

package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostStatisticsDTO;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * statistics service.
 *
 * @author wq li 2021/09/29 14:32
 **/
public interface PostStatisticsService extends ServletBasicService<PostStatisticsDTO, PostStatisticsVO> {

    /**
     * 分页查询
     *
     * @param page 分页
     * @param size 大小
     * @return 结果集
     */
    Page<PostStatisticsVO> retrieve(int page, int size);

}
