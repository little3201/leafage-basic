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

import java.time.LocalDateTime;

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
