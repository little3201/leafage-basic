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

package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Privilege;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * privilege repository
 *
 * @author wilsonli 2023-03-25 09:45
 **/
@Repository
public interface ComponentRepository extends R2dbcRepository<Privilege, Long> {

    /**
     * 查询
     *
     * @return 有效帖子
     */
    Flux<Privilege> findByEnabledTrue(Pageable pageable);

    /**
     * 是否已存在
     *
     * @param componentName 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByComponentName(String componentName);
}
