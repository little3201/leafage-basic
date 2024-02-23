/*
 *  Copyright 2018-2024 little3201.
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
package io.leafage.basic.hypervisor.domain;


import io.leafage.basic.hypervisor.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for group roles.
 *
 * @author wq li 2022/1/26 15:20
 */
@Entity
@Table(name = "group_roles", indexes = {@Index(name = "idx_group_roles_group_id", columnList = "group_id"),
        @Index(name = "idx_group_roles_role_id", columnList = "role_id")})
public class GroupRoles extends AuditMetadata {

    /**
     * group主键
     */
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    /**
     * role主键
     */
    @Column(name = "role_id", nullable = false)
    private Long roleId;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
