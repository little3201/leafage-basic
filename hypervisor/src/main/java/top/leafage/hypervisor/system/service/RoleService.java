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
import top.leafage.hypervisor.system.domain.dto.RoleDTO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.SimplePrivilegeVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;

import java.util.List;
import java.util.Set;

/**
 * role service.
 *
 * @author wq li
 */
public interface RoleService extends JpaCrudService<RoleDTO, RoleVO> {

    /**
     * 添加 user
     *
     * @param id        the pk of group.
     * @param usernames the username of users.
     */
    void addMembers(Long id, Set<String> usernames);

    /**
     * 查询 user
     *
     * @param id the pk of group.
     * @return 数据集
     */
    List<UserVO> members(Long id);

    /**
     * 移除 user
     *
     * @param id        the pk of group.
     * @param usernames the username of users.
     */
    void removeMembers(Long id, Set<String> usernames);

    /**
     * 添加 privilege
     *
     * @param id          the pk of role.
     * @param privilegeId the pk of privilege.
     * @param action      the action of privilege.
     */
    void addPrivilege(Long id, Long privilegeId, String action);

    /**
     * 查询 privilege
     *
     * @param id the pk of role.
     * @return 数据集
     */
    List<SimplePrivilegeVO> privileges(Long id);

    /**
     * 移除 privilege
     *
     * @param id          the pk of role.
     * @param privilegeId the pk of privilege.
     * @param action      the action of privilege.
     */
    void removePrivilege(Long id, Long privilegeId, String action);
}
