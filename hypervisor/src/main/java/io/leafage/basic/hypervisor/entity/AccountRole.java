/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for account role.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
@Entity
@Table(name = "account_role")
public class AccountRole extends AbstractEntity {

    /**
     * 用户主键
     */
    @Column(name = "account_id")
    private Long accountId;
    /**
     * 组主键
     */
    @Column(name = "role_id")
    private Long roleId;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
