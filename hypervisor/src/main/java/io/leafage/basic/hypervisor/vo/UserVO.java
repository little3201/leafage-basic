/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import org.springframework.util.StringUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * VO class for User
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class UserVO extends UserDetailVO implements Serializable {

    private static final long serialVersionUID = 635350278320138075L;

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
     * 修改时间
     */
    private LocalDateTime modifyTime;

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

    /**
     * 手机号脱敏
     *
     * @return 脱敏后的手机号
     */
    public String getPhone() {
        if (StringUtils.hasText(phone)) {
            return phone.replaceAll("(^\\d{3})\\d.*(\\d{4})", "$1****$2");
        }
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 邮箱脱敏
     *
     * @return 脱敏后的邮箱
     */
    public String getEmail() {
        if (StringUtils.hasText(email)) {
            return email.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
        }
        return email;
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

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 安全防护，置空 password 的数据
     *
     * @return null
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 安全防护，置空 password 的数据
     *
     * @param password 密码
     */
    @Override
    public void setPassword(String password) {
        super.setPassword(null);
    }
}
