/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang
 */
@Document(collection = "role_info")
public class RoleInfo {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 角色ID
     */
    @Field(value = "role_id")
    private Long roleId;
    /**
     * 名称
     */
    @Field(value = "name")
    private String name;
    /**
     * 描述
     */
    @Field(value = "description")
    private String description;
    /**
     * 是否有效
     */
    @JsonIgnore
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @JsonIgnore
    @Field(value = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @JsonIgnore
    @Field(value = "modify_time")
    private LocalDateTime modifyTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
