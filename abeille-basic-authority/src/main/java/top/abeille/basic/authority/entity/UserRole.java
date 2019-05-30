/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.entity;

import top.abeille.common.basic.BasicInfo;

import javax.persistence.*;

/**
 * Model class for UserRole
 *
 * @author liwenqiang 2018/12/4 10:09
 **/
@Entity
@Table(name = "user_role")
public class UserRole extends BasicInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 用户主键
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 角色主键
     */
    @Column(name = "role_id")
    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
