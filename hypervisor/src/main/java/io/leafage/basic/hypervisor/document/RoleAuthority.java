/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for Role Authority
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "role_authority")
public class RoleAuthority extends AbstractDocument {

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
}
