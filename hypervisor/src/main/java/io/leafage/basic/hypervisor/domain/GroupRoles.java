/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;


import io.leafage.basic.hypervisor.config.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for group roles.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
@Entity
@Table(name = "group_roles", indexes = {@Index(name = "idx_group_roles_group_id", columnList = "group_id"),
        @Index(name = "idx_group_roles_role_id", columnList = "role_id")})
public class GroupRoles extends AuditMetadata {

    /**
     * 组主键
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
