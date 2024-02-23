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
 * model class for role members.
 *
 * @author wq li 2022/1/26 15:20
 */
@Entity
@Table(name = "role_members", indexes = {@Index(name = "idx_role_members_group_id", columnList = "role_id"),
        @Index(name = "idx_role_members_username", columnList = "username")})
public class RoleMembers extends AuditMetadata {


    /**
     * group主键
     */
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false)
    private String username;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
