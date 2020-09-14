package top.abeille.basic.assets.bo;

import java.io.Serializable;

public class UserBO implements Serializable {

    private static final long serialVersionUID = 3895887694244324648L;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * id
     */
    private String businessId;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
