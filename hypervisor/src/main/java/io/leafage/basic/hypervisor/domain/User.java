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
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * model class for user.
 *
 * @author wq li 2020-12-20 9:54
 */
@Entity
@Table(name = "users")
public class User extends AuditMetadata {

    /**
     * user
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * password
     */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 50)
    private String email;

    /**
     * 头像
     */
    @Column(name = "avatar", length = 100)
    private String avatar;

    /**
     * 账号有效期
     */
    @Column(name = "account_expires_at")
    private Instant accountExpiresAt;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Instant getCredentialsExpiresAt() {
        return credentialsExpiresAt;
    }

    public void setCredentialsExpiresAt(Instant credentialsExpiresAt) {
        this.credentialsExpiresAt = credentialsExpiresAt;
    }
}
