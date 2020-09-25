/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.bo;

import java.io.Serializable;

/**
 * BO class for User
 *
 * @author liwenqiang
 */
public class UserBO implements Serializable {

    private static final long serialVersionUID = 5441310307209449909L;

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
