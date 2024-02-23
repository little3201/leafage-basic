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

import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.vo.PostContentVO;
import io.leafage.basic.assets.vo.PostVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * posts service.
 *
 * @author wq li 2018/12/17 19:26
 **/
public interface PostsService extends ServletBasicService<PostDTO, PostVO> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询结果
     */
    Page<PostVO> retrieve(int page, int size, String sort);

    /**
     * 根据id查询文章详情
     *
     * @param id 主键
     * @return 查询结果
     */
    PostContentVO details(Long id);

    /**
     * 下一篇
     *
     * @param id 主键
     * @return 信息
     */
    PostVO next(Long id);

    /**
     * 上一篇
     *
     * @param id 主键
     * @return 信息
     */
    PostVO previous(Long id);
}
