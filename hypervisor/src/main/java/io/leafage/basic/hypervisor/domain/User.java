/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.domain;


import io.leafage.basic.hypervisor.config.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * model class for user.
 *
 * @author liwenqiang 2020-12-20 9:54
 */
@Entity
@Table(name = "users")
public class User extends AuditMetadata {

    /**
     * user
     */
    @Column(unique = true)
    private String username;

    /**
     * 名
     */
    private String firstname;

    /**
     * 姓
     */
    private String lastname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * user有效期
     */
    @Column(name = "account_expires_at")
    private LocalDateTime accountExpiresAt;

    /**
     * 是否锁定
     */
    @Column(name = "is_account_locked")
    private boolean accountLocked;

    /**
     * 密码有效期
     */
    @Column(name = "credentials_expires_at")
    private LocalDateTime credentialsExpiresAt;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
