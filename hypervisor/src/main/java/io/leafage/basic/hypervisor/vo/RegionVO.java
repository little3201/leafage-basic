/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import java.time.LocalDateTime;

/**
 * VO class for Region
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public class RegionVO extends SuperVO<Long> {

    /**
     * 编号
     */
    private String code;
    /**
     * 邮编
     */
    private Integer postalCode;
    /**
     * 区号
     */
    private String areaCode;
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

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
