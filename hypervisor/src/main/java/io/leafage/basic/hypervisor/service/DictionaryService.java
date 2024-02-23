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

package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

import java.util.List;

/**
 * dictionary service.
 *
 * @author wq li 2022-04-06 17:38
 **/
public interface DictionaryService extends ServletBasicService<DictionaryDTO, DictionaryVO> {


    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<DictionaryVO> retrieve(int page, int size);

    /**
     * 获取子节点
     *
     * @return 数据集
     */
    List<DictionaryVO> lower(Long id);
}
