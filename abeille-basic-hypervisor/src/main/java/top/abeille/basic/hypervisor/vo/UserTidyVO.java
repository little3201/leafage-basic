/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.hypervisor.vo;

import java.io.Serializable;

/**
 * Model class for UserInfo
 *
 * @author liwenqiang
 */
public class UserTidyVO implements Serializable {

    private static final long serialVersionUID = -5782081329816247257L;

    /**
     * 昵称
     */
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
