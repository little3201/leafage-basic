/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for UserRole
 *
 * @author liwenqiang 2019/9/16 10:09
 **/
@Document(collection = "group_user")
public class GroupUser extends BaseDocument {

    /**
     * 用户主键
     */
    @Indexed
    @Field(value = "user_id")
    private String userId;
    /**
     * 组主键
     */
    @Indexed
    @Field(value = "group_id")
    private String groupId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
