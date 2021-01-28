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
@Table(name = "group_user")
public class GroupUser extends BaseEntity {

    /**
     * 用户主键
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 组主键
     */
    @Column(name = "group_id")
    private Long groupId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
