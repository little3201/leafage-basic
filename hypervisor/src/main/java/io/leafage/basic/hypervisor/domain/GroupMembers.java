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
 * model class for group members.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
@Entity
@Table(name = "group_members", indexes = {@Index(name = "idx_group_members_group_id", columnList = "group_id"),
        @Index(name = "idx_group_members_username", columnList = "username")})
public class GroupMembers extends AuditMetadata {

    /**
     * group主键
     */
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false)
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
