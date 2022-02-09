/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for Account Group
 *
 * @author liwenqiang 2019/9/16 10:09
 **/
@Document(collection = "account_group")
public class AccountGroup extends AbstractDocument {

    /**
     * 账号主键
     */
    @Indexed
    @Field(value = "account_id")
    private ObjectId accountId;
    /**
     * 分组主键
     */
    @Indexed
    @Field(value = "group_id")
    private ObjectId groupId;


    public ObjectId getAccountId() {
        return accountId;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public ObjectId getGroupId() {
        return groupId;
    }

    public void setGroupId(ObjectId groupId) {
        this.groupId = groupId;
    }

}
