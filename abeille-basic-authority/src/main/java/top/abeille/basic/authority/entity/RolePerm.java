/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.entity;

import top.abeille.common.basic.BasicInfo;

import javax.persistence.*;

/**
 * Model class for RolePerm
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "role_perm")
public class RolePerm extends BasicInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;
    /**
     * 权限ID
     */
    @Column(name = "perm_id")
    private Long permId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermId() {
        return permId;
    }

    public void setPermId(Long permId) {
        this.permId = permId;
    }

}
