/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;

import java.io.Serializable;

/**
 * DTO class for Region
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public class RegionVO extends AbstractVO<Long> implements Serializable {

    private static final long serialVersionUID = 4445793379366765718L;

    /**
     * 名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 邮编
     */
    private Integer zip;
    /**
     * 上级
     */
    private String superior;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
