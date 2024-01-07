/*
 *  Copyright 2018-2024 the original author or authors.
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

import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.vo.MessageVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * message service
 *
 * @author liwenqiang 2022-02-10 13:49
 */
public interface MessageService extends ReactiveBasicService<MessageDTO, MessageVO> {

    /**
     * 分页查询
     *
     * @param page     页码
     * @param size     大小
     * @param receiver 接收者
     * @return 结果集
     */
    Mono<Page<MessageVO>> retrieve(int page, int size, String receiver);
}
