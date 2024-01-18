/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * model class for account group.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
@Entity
@Table(name = "group_roles")
public class GroupRoles extends AbstractModel {

    /**
     * 组主键
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * role主键
     */
    @Column(name = "role_id")
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
