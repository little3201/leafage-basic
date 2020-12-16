/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for RoleResource
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "role_authority")
public class RoleAuthority extends BaseDocument {

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

}
