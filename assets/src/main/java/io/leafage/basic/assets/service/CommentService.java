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

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

import java.util.List;

/**
 * comment service.
 *
 * @author wq li
 */
public interface CommentService extends ServletBasicService<CommentDTO, CommentVO> {

    Page<CommentVO> retrieve(int page, int size, String sortBy, boolean descending);

    /**
     * 根据posts查询
     *
     * @param id 帖子代码
     * @return 关联的评论
     */
    List<CommentVO> relation(Long id);

    /**
     * 根据replier查询
     *
     * @param id 主键
     * @return 回复的评论
     */
    List<CommentVO> replies(Long id);
}
