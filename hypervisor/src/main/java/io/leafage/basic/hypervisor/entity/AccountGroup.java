/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for account group.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
@Entity
@Table(name = "account_group")
public class AccountGroup extends AbstractEntity {

    /**
     * 账号主键
     */
    @Column(name = "account_id")
    private Long accountId;
    /**
     * 组主键
     */
    @Column(name = "group_id")
    private Long groupId;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
