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
@Table(name = "account_group")
public class GroupMembers extends AbstractModel {

    /**
     * 组主键
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 用户名
     */
    private String username;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
