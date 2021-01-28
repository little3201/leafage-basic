/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for UserGroup
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "user_role")
public class UserRole extends BaseEntity {

    /**
     * 用户主键
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 组主键
     */
    @Column(name = "role_id")
    private Long roleId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
