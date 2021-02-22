/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
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
    private ObjectId roleId;
    /**
     * 资源ID
     */
    @Indexed
    @Field(value = "authority_id")
    private ObjectId authorityId;
    /**
     * 请求方式, 如：GET、POST、PUT、DELETE等
     */
    private String mode;


    public ObjectId getRoleId() {
        return roleId;
    }

    public void setRoleId(ObjectId roleId) {
        this.roleId = roleId;
    }

    public ObjectId getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(ObjectId authorityId) {
        this.authorityId = authorityId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
