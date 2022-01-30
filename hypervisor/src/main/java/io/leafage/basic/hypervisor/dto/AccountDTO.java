package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO class for account.
 *
 * @author liwenqiang 2022/1/26 15:20
 */
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = -3179589790743630299L;

    /**
     * 昵称
     */
    @Size(min = 8, max = 32)
    private String nickname;
    /**
     * 头像
     */
    private String avatar;


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
}
