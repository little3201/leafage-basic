/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for RoleSource
 *
 * @author liwenqiang
 */
@Document(collection = "role_source")
public class RoleSource {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 角色ID
     */
    @Indexed
    @Field(value = "role_id")
    private Long roleId;
    /**
     * 资源ID
     */
    @Indexed
    @Field(value = "source_id")
    private Long sourceId;

    /**
     * 是否有效
     */
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Field(value = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
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

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
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
