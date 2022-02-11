/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * DTO class for Region
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public class RegionDTO implements Serializable {

    private static final long serialVersionUID = -2220657046421273787L;

    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 简称
     */
    private String alias;
    /**
     * 邮编
     */
    private Integer postalCode;
    /**
     * 区号
     */
    private String areaCode;
    /**
     * 描述
     */
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
