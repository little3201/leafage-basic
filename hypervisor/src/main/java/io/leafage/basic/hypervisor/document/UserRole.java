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
@Document(collection = "user_role")
public class UserRole extends BaseDocument {

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
    @Field(value = "role_id")
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
