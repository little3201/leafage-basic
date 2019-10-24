package top.abeille.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户关键信息类
 *
 * @author liwenqiang 2019/8/31 15:50
 **/
public class UserDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 组织主键
     */
    @NotNull
    private Long groupId;
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
    private String username;
    /**
     * 电话
     */
    @NotBlank
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 地址
     */
    private String address;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
