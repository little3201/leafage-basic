/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import io.leafage.basic.hypervisor.bo.AuthorityBO;

import java.time.LocalDateTime;

/**
 * VO class for Authority
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AuthorityVO extends AuthorityBO {

    /**
     * 编号
     */
    private String code;
    /**
     * 统计数
     */
    private Long count;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
