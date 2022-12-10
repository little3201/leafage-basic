/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.UserBO;

import java.time.LocalDateTime;

/**
 * VO class for User
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class UserVO extends UserBO {

    /**
     * 账号
     */
    private String username;
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
