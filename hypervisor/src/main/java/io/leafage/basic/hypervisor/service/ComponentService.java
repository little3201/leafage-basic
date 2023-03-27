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

import io.leafage.basic.hypervisor.dto.ComponentDTO;
import io.leafage.basic.hypervisor.vo.ComponentVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;
import top.leafage.common.reactive.ReactiveBasicService;

import java.util.List;

/**
 * component service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface ComponentService extends ReactiveBasicService<ComponentDTO, ComponentVO> {

    /**
     * 查询构造树结构的数据
     *
     * @return 数据集
     */
    Mono<List<TreeNode>> tree();

    /**
     * 查询用户权限
     *
     * @param username 用户名
     * @return 权限树
     */
    Mono<List<TreeNode>> components(String username);

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<ComponentVO>> retrieve(int page, int size);
}
