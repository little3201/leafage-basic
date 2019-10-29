/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for UserInfo
 *
 * @author liwenqiang
 */
@Document(collection = "user_info")
public class UserInfo {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 用户ID
     */
    @Indexed
    @Field(value = "user_id")
    private Long userId;
    /**
     * 角色主键
     */
    @Indexed
    @Field(value = "role_id")
    private Long roleId;
    /**
     * 昵称
     */
    @Indexed
    @Field(value = "nickname")
    private String nickname;
    /**
     * 头像
     */
    @Field(value = "avatar")
    private String avatar;
    /**
     * 用户名
     */
    @Indexed
    @Field(value = "username")
    private String username;
    /**
     * 密码
     */
    @Field(value = "password")
    private String password;
    /**
     * 电话
     */
    @Field(value = "mobile")
    private String mobile;
    /**
     * 邮箱
     */
    @Field(value = "email")
    private String email;
    /**
     * 地址
     */
    @Field(value = "address")
    private String address;
    /**
     * 是否有效
     */
    @Field(value = "is_account_non_expired")
    private Boolean accountNonExpired;
    /**
     * 是否锁定
     */
    @Field(value = "is_account_non_locked")
    private Boolean accountNonLocked;
    /**
     * 密码是否有效
     */
    @Field(value = "is_credentials_non_expired")
    private Boolean credentialsNonExpired;

    /**
     * 是否有效
     */
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Field(value = "modifier")
    private Long modifier;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
