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


import top.leafage.common.servlet.audit.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * model class for user.
 *
 * @author wq li
 */
@Entity
@Table(name = "users")
public class User extends AuditMetadata {

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "avatar", length = 100)
    private String avatar;

    @Column(name = "account_expires_at")
    private Instant accountExpiresAt;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_expires_at")
    private Instant credentialsExpiresAt;

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Setter for the field <code>username</code>.</p>
     *
     * @param username a {@link java.lang.String} object
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Getter for the field <code>password</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Setter for the field <code>password</code>.</p>
     *
     * @param password a {@link java.lang.String} object
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * <p>Getter for the field <code>email</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEmail() {
        return email;
    }

    /**
     * <p>Setter for the field <code>email</code>.</p>
     *
     * @param email a {@link java.lang.String} object
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * <p>Getter for the field <code>avatar</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * <p>Setter for the field <code>avatar</code>.</p>
     *
     * @param avatar a {@link java.lang.String} object
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * <p>Getter for the field <code>accountExpiresAt</code>.</p>
     *
     * @return a {@link java.time.Instant} object
     */
    public Instant getAccountExpiresAt() {
        return accountExpiresAt;
    }

    /**
     * <p>Setter for the field <code>accountExpiresAt</code>.</p>
     *
     * @param accountExpiresAt a {@link java.time.Instant} object
     */
    public void setAccountExpiresAt(Instant accountExpiresAt) {
        this.accountExpiresAt = accountExpiresAt;
    }

    /**
     * <p>isAccountNonLocked.</p>
     *
     * @return a boolean
     */
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * <p>Setter for the field <code>accountNonLocked</code>.</p>
     *
     * @param accountNonLocked a boolean
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * <p>Getter for the field <code>credentialsExpiresAt</code>.</p>
     *
     * @return a {@link java.time.Instant} object
     */
    public Instant getCredentialsExpiresAt() {
        return credentialsExpiresAt;
    }

    /**
     * <p>Setter for the field <code>credentialsExpiresAt</code>.</p>
     *
     * @param credentialsExpiresAt a {@link java.time.Instant} object
     */
    public void setCredentialsExpiresAt(Instant credentialsExpiresAt) {
        this.credentialsExpiresAt = credentialsExpiresAt;
    }
}
