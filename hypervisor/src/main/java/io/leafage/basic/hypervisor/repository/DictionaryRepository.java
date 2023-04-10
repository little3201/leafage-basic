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

import io.leafage.basic.hypervisor.domain.Dictionary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * dictionary repository
 *
 * @author liwenqiang 2022-03-30 07:29
 **/
@Repository
public interface DictionaryRepository extends R2dbcRepository<Dictionary, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效数据集
     */
    Flux<Dictionary> findByEnabledTrue(Pageable pageable);

    /**
     * 是否已存在
     *
     * @param dictionaryName 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByDictionaryName(String dictionaryName);

    /**
     * 查询下级
     *
     * @return 结果信息
     */
    Flux<Dictionary> findBySuperiorId(Long superiorId);

    /**
     * 查询上级
     *
     * @return 结果信息
     */
    Flux<Dictionary> findBySuperiorIsNull();
}
