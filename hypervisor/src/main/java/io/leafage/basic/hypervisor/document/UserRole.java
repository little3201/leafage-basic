/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for User Role
 *
 * @author liwenqiang 2019/9/16 10:09
 **/
@Document(collection = "user_role")
public class UserRole extends AbstractDocument {

    /**
     * 用户主键
     */
    @Indexed
    @Field(value = "user_id")
    private ObjectId userId;
    /**
     * 组主键
     */
    @Indexed
    @Field(value = "role_id")
    private ObjectId roleId;

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getRoleId() {
        return roleId;
    }

    public void setRoleId(ObjectId roleId) {
        this.roleId = roleId;
    }

}
