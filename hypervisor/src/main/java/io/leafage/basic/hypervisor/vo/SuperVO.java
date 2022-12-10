/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.SuperBO;

import java.time.LocalDateTime;

/**
 * VO class
 *
 * @author liwenqiang 2022-12-09 22:23
 */
public abstract class SuperVO<T> extends SuperBO<T> {

    /**
     * 编号
     */
    private String code;
    /**
     * 统计数
     */
    private long count;
    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
