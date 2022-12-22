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

package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.vo.AccountVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * account group service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccountGroupService {

    /**
     * 查询关联账号
     *
     * @param code 代码
     * @return 数据集
     */
    Flux<AccountVO> accounts(String code);

    /**
     * 查询关联分组
     *
     * @param code 代码
     * @return 数据集
     */
    Mono<List<String>> groups(String code);

    /**
     * 保存用户-分组关系
     *
     * @param username 用户
     * @param groups   分组信息
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(String username, Set<String> groups);
}
