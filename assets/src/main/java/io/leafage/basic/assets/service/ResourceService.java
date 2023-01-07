/*
 *  Copyright 2018-2022 the original author or authors.
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

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.vo.ResourceVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * resource service
 *
 * @author liwenqiang 2020-02-24 11:59
 **/
public interface ResourceService extends ReactiveBasicService<ResourceDTO, ResourceVO, String> {

    /**
     * 分页查询
     *
     * @param page     页码
     * @param size     大小
     * @param sort     排序
     * @param category 分类
     * @return 结果集
     */
    Mono<Page<ResourceVO>> retrieve(int page, int size, String sort, String category);

}
