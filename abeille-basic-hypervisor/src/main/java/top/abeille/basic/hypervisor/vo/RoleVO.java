/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang
 */
public class RoleVO implements Serializable {

    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 修改人
     */
    private Long modifier;
    /**
     * 修改时间
     */
    private LocalDate modifyTime;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDate getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDate modifyTime) {
        this.modifyTime = modifyTime;
    }
}
