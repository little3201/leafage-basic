package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * DTO class for User
 *
 * @author liwenqiang 2019/8/31 15:50
 **/
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -1165865988174734554L;

    /**
     * 昵称
     */
    @NotBlank
    @Size(min = 8, max = 16)
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 电话
     */
    @NotBlank
    @Pattern(regexp = "0?(13|14|15|17|18|19)[0-9]{9}")
    private String phone;
    /**
     * 邮箱
     */
    @NotBlank
    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}")
    private String email;
    /**
     * 性别: 0-未知 1-女 2-男
     */
    private int gender;
    /**
     * 地址
     */
    private String address;
    /**
     * 角色列表
     */
    private Set<String> roles = Collections.emptySet();
    /**
     * 修改人
     */
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
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
