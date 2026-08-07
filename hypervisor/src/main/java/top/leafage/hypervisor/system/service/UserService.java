/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.leafage.hypervisor.system.service;

import top.leafage.common.data.jpa.JpaCrudService;
import top.leafage.hypervisor.system.domain.dto.UserDTO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;

import java.util.List;
import java.util.Set;

/**
 * User service.
 *
 * @author wq li
 */
public interface UserService extends JpaCrudService<UserDTO, UserVO> {

    /**
     * Fetch me
     * @return user
     */
    UserVO fetch();

    /**
     * 添加 role
     *
     * @param id      the pk of group.
     * @param roleIds the pk of roles.
     */
    void addRoles(Long id, Set<Long> roleIds);

    /**
     * 查询 role
     *
     * @param id the pk of group.
     * @return 数据集
     */
    List<RoleVO> roles(Long id);

    /**
     * 移除 role
     *
     * @param id      the pk of group.
     * @param roleIds the pk of roles.
     */
    void removeRoles(Long id, Set<Long> roleIds);

}
