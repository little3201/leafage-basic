package top.abeille.basic.hypervisor.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户关键信息类
 *
 * @author liwenqiang 2019/8/31 15:50
 **/
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -1165865988174734554L;
    /**
     * 角色ID
     */
    @NotNull
    private Long roleId;
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
     * 用户名
     */
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "contains illegal format")
    private String username;
    /**
     * 电话
     */
    @NotBlank
    @Pattern(regexp = "^([1][3-9])\\d{9}$", message = "mobile can't be accepted")
    private String mobile;
    /**
     * 邮箱
     */
    @Email(message = "illegal format")
    private String email;
    /**
     * 地址
     */
    private String address;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
