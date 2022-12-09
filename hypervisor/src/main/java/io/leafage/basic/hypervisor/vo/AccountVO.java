/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import java.time.LocalDateTime;

/**
 * VO class for Account
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AccountVO {

    /**
     * 账号
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号有效期
     */
    private LocalDateTime accountExpiresAt;
    /**
     * 是否锁定
     */
    private boolean accountLocked;
    /**
     * 密码有效期
     */
    private LocalDateTime credentialsExpiresAt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public LocalDateTime getAccountExpiresAt() {
        return accountExpiresAt;
    }

    public void setAccountExpiresAt(LocalDateTime accountExpiresAt) {
        this.accountExpiresAt = accountExpiresAt;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public LocalDateTime getCredentialsExpiresAt() {
        return credentialsExpiresAt;
    }

    public void setCredentialsExpiresAt(LocalDateTime credentialsExpiresAt) {
        this.credentialsExpiresAt = credentialsExpiresAt;
    }
}
