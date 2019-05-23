/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import top.abeille.basic.authority.view.UserView;

import javax.persistence.*;
import java.util.Date;

/**
 * Model class for UserInfo
 * @author liwenqiang
 */
@Entity
@Table(name = "user_info")
public class UserInfoModel {

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 组织主键
     */
    private Long groupId;
    /**
     * 中文姓名
     */
    @JsonView(UserView.Summary.class)
    private String userNameCn;
    /**
     * 英文姓名
     */
    @JsonView(UserView.Summary.class)
    private String userNameEn;
    /**
     * 用户名
     */
    @JsonView(UserView.Summary.class)
    private String username;
    /**
     * 密码
     */
    @JsonIgnore
    private String password;
    /**
     * 电话
     */
    @JsonView(UserView.Summary.class)
    private String userMobile;
    /**
     * 邮箱
     */
    @JsonView(UserView.Summary.class)
    private String userEmail;
    /**
     * 地址
     */
    @JsonView(UserView.Summary.class)
    private String userAddress;
    /**
     * 是否无效
     */
    @JsonIgnore
    private Boolean accountNonExpired;
    /**
     * 是否没有锁定
     */
    @JsonIgnore
    private Boolean accountNonLocked;
    /**
     * 密码是否有效
     */
    @JsonIgnore
    private Boolean credentialsNonExpired;
    /**
     * 是否可用
     */
    @JsonIgnore
    private Boolean enabled;
    /**
     * 修改人ID
     */
    @JsonIgnore
    private Long modifierId;
    /**
     * 修改时间
     */
    @JsonIgnore
    private Date modifyTime;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Column(name = "user_name_cn")
    public String getUserNameCn() {
        return userNameCn;
    }

    public void setUserNameCn(String userNameCn) {
        this.userNameCn = userNameCn;
    }

    @Column(name = "user_name_en")
    public String getUserNameEn() {
        return userNameEn;
    }

    public void setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "user_mobile")
    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    @Column(name = "user_email")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Column(name = "user_address")
    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    @Column(name = "is_account_non_expired")
    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Column(name = "is_account_non_locked")
    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Column(name = "is_credentials_non_expired")
    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Column(name = "is_enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "modifier_id")
    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    @Column(name = "modify_time")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
