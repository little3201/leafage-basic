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

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * comment service
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface CommentService extends ReactiveBasicService<CommentDTO, CommentVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<CommentVO>> retrieve(int page, int size);

    /**
     * 查询
     *
     * @param code 帖子代码
     * @return 关联的评论
     */
    Flux<CommentVO> relation(String code);

    /**
     * 查询回复
     *
     * @param replier 回复代码
     * @return 回复的评论
     */
    Flux<CommentVO> replies(String replier);

}
