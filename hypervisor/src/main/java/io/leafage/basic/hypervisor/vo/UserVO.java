/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * VO class for User
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class UserVO implements Serializable {

    private static final long serialVersionUID = 635350278320138075L;

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
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别: null-未知 F-女 M-男
     */
    private Character gender;
    /**
     * 出生日期
     */
    private LocalDate birthday;
    /**
     * 是否有效
     */
    private boolean accountNonExpired;
    /**
     * 是否锁定
     */
    private boolean accountNonLocked;
    /**
     * 密码是否有效
     */
    private boolean credentialsNonExpired;

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

    public String getPhone() {
        return phone.replaceAll("(^\\d{3})\\d.*(\\d{4})", "$1****$2");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
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
