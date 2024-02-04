/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for role privileges.
 *
 * @author wq li 2022/1/26 15:20
 */
@Entity
@Table(name = "role_privileges", indexes = {@Index(name = "idx_role_privileges_role_id", columnList = "role_id"),
        @Index(name = "idx_role_privileges_privilege_id", columnList = "privilege_id")})
public class RolePrivileges extends AuditMetadata {

    /**
     * role主键
     */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    /**
     * 资源主键
     */
    @Column(name = "privilege_id", nullable = false)
    private Long privilegeId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }
}
