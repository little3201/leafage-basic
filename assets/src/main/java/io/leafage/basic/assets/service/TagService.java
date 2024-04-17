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

import io.leafage.basic.assets.dto.TagDTO;
import io.leafage.basic.assets.vo.TagVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * tag service.
 *
 * @author wq li  2020-12-03 22:59
 **/
public interface TagService extends ServletBasicService<TagDTO, TagVO> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询结果
     */
    Page<TagVO> retrieve(int page, int size, String sort);
}
