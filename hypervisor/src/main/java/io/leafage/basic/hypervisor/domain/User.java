/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.leafage.basic.hypervisor.domain;


import io.leafage.basic.hypervisor.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * model class for user.
 *
 * @author wq li 2020-12-20 9:54
 */
@Entity
@Table(name = "users", indexes = {@Index(name = "uni_users_username", columnList = "username")})
public class User extends AuditMetadata {

    /**
     * user
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * password
     */
    @Column(name = "password", nullable = false)
    private String password;

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
     * 有效
     */
    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;

    /**
     * 有效期
     */
    @Column(name = "account_expires_at")
    private Instant accountExpiresAt;

    /**
     * 未锁定
     */
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    /**
     * 密码有效
     */
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    /**
     * 密码有效期
     */
    @Column(name = "credentials_expires_at")
    private Instant credentialsExpiresAt;


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

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Instant getAccountExpiresAt() {
        return accountExpiresAt;
    }

    public void setAccountExpiresAt(Instant accountExpiresAt) {
        this.accountExpiresAt = accountExpiresAt;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Instant getCredentialsExpiresAt() {
        return credentialsExpiresAt;
    }

    public void setCredentialsExpiresAt(Instant credentialsExpiresAt) {
        this.credentialsExpiresAt = credentialsExpiresAt;
    }
}
