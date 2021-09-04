/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import java.time.LocalDateTime;

/**
 * DTO class for Region
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public class RegionVO {

    /**
     * 代码
     */
    private Long code;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 名称
     */
    private String name;
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
