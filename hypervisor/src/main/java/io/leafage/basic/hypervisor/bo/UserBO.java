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

package io.leafage.basic.hypervisor.bo;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

/**
 * bo class for User
 *
 * @author wq li
 */
public abstract class UserBO {

    /**
     * 用户名
     */
    @NotBlank(message = "username is blank.")
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
    private Instant accountExpiresAt;

    /**
     * 是否锁定
     */
    private boolean accountNonLocked;

    /**
     * 密码有效期
     */
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
     * <p>Getter for the field <code>firstname</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * <p>Setter for the field <code>firstname</code>.</p>
     *
     * @param firstname a {@link java.lang.String} object
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * <p>Getter for the field <code>lastname</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * <p>Setter for the field <code>lastname</code>.</p>
     *
     * @param lastname a {@link java.lang.String} object
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
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
