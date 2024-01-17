/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for role privilege.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
@Entity
@Table(name = "role_privilege")
public class RolePrivileges extends AbstractModel {

    /**
     * role主键
     */
    @Column(name = "role_id")
    private Long roleId;
    /**
     * 资源主键
     */
    @Column(name = "privilege_id")
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
