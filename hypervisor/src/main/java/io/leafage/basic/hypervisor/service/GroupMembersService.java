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

import io.leafage.basic.hypervisor.domain.GroupMembers;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * group members service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface GroupMembersService {

    /**
     * 查询关联账号
     *
     * @param groupId group主键
     * @return 数据集
     */
    Mono<List<GroupMembers>> members(Long groupId);

    /**
     * 查询关联分组
     *
     * @param code 代码
     * @return 数据集
     */
    Mono<List<GroupMembers>> groups(String code);

    /**
     * 分组-账号关系
     *
     * @param username 账号
     * @param groupIds 分组主键集合
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(String username, Set<Long> groupIds);
}
