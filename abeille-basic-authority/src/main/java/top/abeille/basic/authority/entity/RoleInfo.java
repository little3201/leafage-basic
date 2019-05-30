/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.entity;

import top.abeille.common.basic.BasicInfo;

import javax.persistence.*;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "role_info")
public class RoleInfo extends BasicInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;
    /**
     * 角色描述
     */
    @Column(name = "role_desc")
    private String roleDesc;
    /**
     * 备注
     */
    @Column(name = "role_remark")
    private String roleRemark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getRoleRemark() {
        return roleRemark;
    }

    public void setRoleRemark(String roleRemark) {
        this.roleRemark = roleRemark;
    }

}
