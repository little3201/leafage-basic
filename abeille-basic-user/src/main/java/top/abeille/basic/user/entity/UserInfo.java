/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import top.abeille.basic.user.view.UserView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Model class for UserInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "user_info")
public class UserInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    /**
     * 用户ID
     */
    @NotNull
    @Column(name = "user_id")
    private String userId;
    /**
     * 组织主键
     */
    @Column(name = "group_id")
    private Long groupId;
    /**
     * 中文姓名
     */
    @JsonView(UserView.Summary.class)
    @Column(name = "user_name_cn")
    private String userNameCn;
    /**
     * 英文姓名
     */
    @JsonView(UserView.Summary.class)
    @Column(name = "user_name_en")
    private String userNameEn;
    /**
     * 用户名
     */
    @JsonView(UserView.Summary.class)
    @Column(name = "username")
    private String username;
    /**
     * 密码
     */
    @JsonIgnore
    @Column(name = "password")
    private String password;
    /**
     * 电话
     */
    @JsonView(UserView.Summary.class)
    @Column(name = "user_mobile")
    private String userMobile;
    /**
     * 邮箱
     */
    @JsonView(UserView.Summary.class)
    @Column(name = "user_email")
    private String userEmail;
    /**
     * 地址
     */
    @JsonView(UserView.Summary.class)
    @Column(name = "user_address")
    private String userAddress;
    /**
     * 是否无效
     */
    @JsonIgnore
    @Column(name = "is_account_non_expired")
    private Boolean accountNonExpired;
    /**
     * 是否没有锁定
     */
    @Column(name = "is_account_non_locked")
    private Boolean accountNonLocked;
    /**
     * 密码是否有效
     */
    @JsonIgnore
    @Column(name = "is_credentials_non_expired")
    private Boolean credentialsNonExpired;

    /**
     * 是否有效
     */
    @JsonIgnore
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人ID
     */
    @JsonIgnore
    @Column(name = "modifier_id")
    private Long modifierId;
    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUserNameCn() {
        return userNameCn;
    }

    public void setUserNameCn(String userNameCn) {
        this.userNameCn = userNameCn;
    }

    public String getUserNameEn() {
        return userNameEn;
    }

    public void setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn;
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

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
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

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
