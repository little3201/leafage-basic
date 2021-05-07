/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * DTO class for User
 *
 * @author liwenqiang 2019/8/31 15:50
 **/
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -1165865988174734554L;

    /**
     * 账号
     */
    @NotBlank
    @Size(min = 4, max = 16)
    private String username;
    /**
     * 昵称
     */
    @Size(min = 8, max = 16)
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
    @Email
    private String email;
    /**
     * 性别: null-未知 0-女 1-男
     */
    private Character gender;
    /**
     * 地址
     */
    private String address;
    /**
     * 角色列表
     */
    private Set<String> roles;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
