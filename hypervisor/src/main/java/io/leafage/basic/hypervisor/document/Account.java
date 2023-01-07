/*
 *  Copyright 2018-2022 the original author or authors.
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

package io.leafage.basic.hypervisor.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for Account
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "account")
public class Account extends AbstractDocument {

    /**
     * 用户名
     */
    @Indexed(unique = true)
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号有效期
     */
    @Field(name = "account_expires_at")
    private LocalDateTime accountExpiresAt;
    /**
     * 是否锁定
     */
    @Field(name = "is_account_locked")
    private boolean accountLocked;
    /**
     * 密码有效期
     */
    @Field(name = "credentials_expires_at")
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
