/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.document;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * Model class for UserInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "user")
public class User extends BaseDocument {

    /**
     * 账号
     */
    @Indexed(unique = true)
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
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String gender;
    /**
     * 出生日期
     */
    private LocalDate birthday;
    /**
     * 账户是否有效
     */
    @Field(name = "is_account_non_expired")
    private boolean accountNonExpired = true;
    /**
     * 是否锁定
     */
    @Field(name = "is_account_non_locked")
    private boolean accountNonLocked = true;
    /**
     * 认证是否有效
     */
    @Field(name = "is_credentials_non_expired")
    private boolean credentialsNonExpired = true;

    public String getGender() {
        return gender;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender.toUpperCase()).name();
    }

    enum Gender {
        /**
         * 男
         */
        FEMALE,
        /**
         * 女
         */
        MALE,
        /**
         * 未知
         */
        @JsonEnumDefaultValue
        UNKNOWN
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
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

}
