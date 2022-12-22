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

import io.leafage.basic.hypervisor.vo.RoleVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * role authority service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface RoleAuthorityService {

    /**
     * 查询关联权限
     *
     * @param code 角色代码
     * @return 数据集
     */
    Mono<List<String>> authorities(String code);

    /**
     * 查询关联角色
     *
     * @param code 权限代码
     * @return 数据集
     */
    Flux<RoleVO> roles(String code);

    /**
     * 保存角色-权限关系
     *
     * @param code        角色代码
     * @param authorities 权限信息
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(String code, Set<String> authorities);
}
