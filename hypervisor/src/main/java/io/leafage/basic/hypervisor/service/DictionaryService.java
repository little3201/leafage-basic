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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * dictionary service
 *
 * @author liwenqiang 2022-03-30 07:34
 **/
public interface DictionaryService extends ReactiveBasicService<DictionaryDTO, DictionaryVO> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<DictionaryVO>> retrieve(int page, int size);

    /**
     * 获取上级
     *
     * @return 数据集
     */
    Flux<DictionaryVO> superior();

    /**
     * 获取下级
     *
     * @param id 主键
     * @return 数据集
     */
    Flux<DictionaryVO> subordinates(Long id);

}
