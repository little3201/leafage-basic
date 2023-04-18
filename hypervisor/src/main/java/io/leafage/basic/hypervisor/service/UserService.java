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

package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * user service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends ReactiveBasicService<UserDTO, UserVO> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Mono<Page<UserVO>> retrieve(int page, int size);

    /**
     * 修改
     *
     * @param username 账号
     * @param userDTO  对象信息
     * @return 操作结果
     */
    Mono<UserVO> modify(String username, UserDTO userDTO);

    /**
     * 查询
     *
     * @param username 账号
     * @return 查询结果
     */
    Mono<UserVO> fetch(String username);

    /**
     * 删除
     *
     * @param username 账号
     * @return 操作结果
     */
    Mono<Void> remove(String username);
}
