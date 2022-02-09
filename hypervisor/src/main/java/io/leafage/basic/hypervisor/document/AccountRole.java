/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for Account Role
 *
 * @author liwenqiang 2019/9/16 10:09
 **/
@Document(collection = "account_role")
public class AccountRole extends AbstractDocument {

    /**
     * 账号主键
     */
    @Indexed
    @Field(value = "account_id")
    private ObjectId accountId;
    /**
     * 角色主键
     */
    @Indexed
    @Field(value = "role_id")
    private ObjectId roleId;

    public ObjectId getAccountId() {
        return accountId;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public ObjectId getRoleId() {
        return roleId;
    }

    public void setRoleId(ObjectId roleId) {
        this.roleId = roleId;
    }

}
