/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import io.leafage.basic.assets.bo.SuperBO;

import java.time.LocalDateTime;

/**
 * VO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostVO extends SuperBO {

    /**
     * 编号
     */
    private String code;
    /**
     * 时间
     */
    private LocalDateTime modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
