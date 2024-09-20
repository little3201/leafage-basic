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
import io.leafage.basic.assets.vo.PostVO;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * posts service
 *
 * @author wq li
 */
public interface PostService extends ReactiveBasicService<PostDTO, PostVO> {

    /**
     * 分页查询
     *
     * @param page       页码
     * @param size       大小
     * @param sort       排序
     * @param categoryId 分类ID
     * @return 结果集
     */
    Mono<Page<PostVO>> retrieve(int page, int size, String sort, @Nullable Long categoryId);

    /**
     * 全文搜索
     *
     * @param keyword 关键字
     * @return 匹配结果
     */
    Flux<PostVO> search(String keyword);
}
