/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang
 */
public class RoleDTO implements Serializable {

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
