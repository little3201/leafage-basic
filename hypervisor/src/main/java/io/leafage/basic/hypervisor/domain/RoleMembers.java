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
 * model class for role members.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
@Entity
@Table(name = "role_members", indexes = {@Index(name = "idx_role_members_group_id", columnList = "role_id"),
        @Index(name = "idx_role_members_username", columnList = "username")})
public class RoleMembers extends AuditMetadata {


    /**
     * 组主键
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
