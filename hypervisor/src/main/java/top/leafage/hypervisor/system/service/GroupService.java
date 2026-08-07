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

import org.jspecify.annotations.NonNull;
import top.leafage.common.data.core.domain.TreeNode;
import top.leafage.common.data.jpa.JpaCrudService;
import top.leafage.hypervisor.system.domain.dto.GroupDTO;
import top.leafage.hypervisor.system.domain.dto.PrivilegeActionsDTO;
import top.leafage.hypervisor.system.domain.vo.GroupVO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.PrivilegeActionsVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Group service.
 *
 * @author wq li
 */
public interface GroupService extends JpaCrudService<GroupDTO, GroupVO> {

    /**
     * 查询树
     *
     * @return result.
     */
    List<TreeNode<@NonNull Long>> tree();

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
     * @param id      the pk.
     * @param roleIds the pk of roles.
     */
    void removeRoles(Long id, Set<Long> roleIds);

    /**
     * 添加 privilege
     *
     * @param id          the pk.
     * @param dtos privilege actions dto.
     */
    void authorize(Long id, Collection<PrivilegeActionsDTO> dtos);

    /**
     * 查询 privilege
     *
     * @param id the pk.
     * @return 数据集
     */
    List<PrivilegeActionsVO> privileges(Long id);

    /**
     * 移除 privilege
     *
     * @param id          the pk.
     * @param privilegeId the pk of privilege.
     * @param action      the action of privilege.
     */
    void removePrivilege(Long id, Long privilegeId, String action);
}
