/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.vo;

/**
 * 文件描述
 *
 * @author liwenqiang  2019-03-03 22:59
 **/
public class UserInfoVO {

    /**
     * 用户编号
     */
    private String userId;
    /**
     * 中文姓名
     */
    private String userNameCn;
    /**
     * 英文姓名
     */
    private String userNameEn;
    /**
     * 电话
     */
    private String userMobile;
    /**
     * 邮箱
     */
    private String userEmail;
    /**
     * 地址
     */
    private String userAddress;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNameCn() {
        return userNameCn;
    }

    public void setUserNameCn(String userNameCn) {
        this.userNameCn = userNameCn;
    }

    public String getUserNameEn() {
        return userNameEn;
    }

    public void setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    @Override
    public String toString() {
        return "UserInfoVO{" +
                "userId='" + userId + '\'' +
                ", userNameCn='" + userNameCn + '\'' +
                ", userNameEn='" + userNameEn + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userAddress='" + userAddress + '\'' +
                '}';
    }
}