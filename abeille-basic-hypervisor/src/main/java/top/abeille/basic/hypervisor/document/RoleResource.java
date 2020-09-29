/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for RoleResource
 *
 * @author liwenqiang
 */
@Document(collection = "role_resource")
public class RoleResource {

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
    private String roleId;
    /**
     * 资源ID
     */
    @Indexed
    @Field(value = "resource_id")
    private String resourceId;
    /**
     * 是否可写
     */
    private boolean hasWrite;
    /**
     * 修改人
     */
    private String modifier;
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public boolean getHasWrite() {
        return hasWrite;
    }

    public void setHasWrite(boolean hasWrite) {
        this.hasWrite = hasWrite;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
