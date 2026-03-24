/*
 * Copyright (c) 2024-2025.  little3201.
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
package top.leafage.hypervisor.system.domain.vo;

import top.leafage.hypervisor.system.domain.GroupPrivilege;
import top.leafage.hypervisor.system.domain.RolePrivilege;

import java.util.Set;

/**
 * vo class for privilege.
 *
 * @author wq li
 */
public record SimplePrivilegeVO(
        Long id,
        Long privilegeId,
        Set<String> actions
) {
    public static SimplePrivilegeVO from(RolePrivilege entity) {
        return new SimplePrivilegeVO(
                entity.getId(),
                entity.getPrivilege().getId(),
                entity.getActions()
        );
    }

    public static SimplePrivilegeVO from(GroupPrivilege entity) {
        return new SimplePrivilegeVO(
                entity.getId(),
                entity.getPrivilege().getId(),
                entity.getActions()
        );
    }
}
