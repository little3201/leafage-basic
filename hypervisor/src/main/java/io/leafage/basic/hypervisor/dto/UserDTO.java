/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO class for User
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -2259398095472923567L;

    /**
     * 账号
     */
    @Size(min = 4, max = 16)
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    @Size(max = 16)
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 电话
     */
    @Pattern(regexp = "0?(13|14|15|17|18|19)[0-9]{9}")
    private String phone;
    /**
     * 邮箱
     */
    @NotBlank
    @Email
    private String email;
    /**
     * 性别: null-未知 F-女 M-男
     */
    private Character gender;
    /**
     * 修改人
     */
    private String modifier;


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

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
