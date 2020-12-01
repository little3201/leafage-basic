/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Set;

/**
 * Model class for UserInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -2259398095472923567L;

    /**
     * 昵称
     */
    @NotBlank
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 电话
     */
    @Pattern(regexp = "0?(13|14|15|17|18|19)[0-9]{9}", message = "mobile not pattern")
    private String mobile;
    /**
     * 邮箱
     */
    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", message = "email not pattern")
    private String email;
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
    @NotBlank
    private String modifier;


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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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