/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "role_info")
public class RoleInfo {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private String roleId;
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 是否有效
     */
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Column(name = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
