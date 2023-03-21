/*
 *  Copyright 2018-2023 the original author or authors.
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
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * posts service
 *
 * @author liwenqiang 2018-12-17 19:26
 **/
public interface PostService extends ReactiveBasicService<PostDTO, PostVO, String> {

    /**
     * 分页查询
     *
     * @param page     页码
     * @param size     大小
     * @param sort     排序
     * @param category 分类
     * @return 结果集
     */
    Mono<Page<PostVO>> retrieve(int page, int size, String sort, String category);

    /**
     * 下一条记录
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<PostVO> next(String code);

    /**
     * 上一条记录
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<PostVO> previous(String code);

    /**
     * 全文搜索
     *
     * @param keyword 关键字
     * @return 匹配结果
     */
    Flux<PostVO> search(String keyword);
}
