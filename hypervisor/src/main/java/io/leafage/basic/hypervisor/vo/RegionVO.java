/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

/**
 * VO class for Region
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public class RegionVO extends SuperVO<Long> {

    /**
     * 邮编
     */
    private Integer postalCode;
    /**
     * 区号
     */
    private String areaCode;


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

}
