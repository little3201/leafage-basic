/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for UserRole
 *
 * @author liwenqiang 2019/9/16 10:09
 **/
@Document(collection = "user_role")
public class UserRole {

    /**
     * 主键
     */
    @Id
    private String id;
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
    /**
     * 修改人
     */
    private String modifier;
    /**
     * 修改时间
     */
    @Field(value = "modify_time")
    @LastModifiedDate
    private LocalDateTime modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
