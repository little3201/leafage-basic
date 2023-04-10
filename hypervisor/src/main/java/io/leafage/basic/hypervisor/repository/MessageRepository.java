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

package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * message repository
 *
 * @author liwenqiang 2022-02-10 13:49
 */
@Repository
public interface MessageRepository extends R2dbcRepository<Message, Long> {

    /**
     * 分页查询
     *
     * @param receiver 接收者
     * @param pageable 分页参数
     * @return 有效数据集
     */
    Flux<Message> findByReceiver(String receiver, Pageable pageable);

    /**
     * 查询未读记录数
     *
     * @return 记录数
     */
    Mono<Long> countByReceiver(String receiver);
}
