/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

/**
 * DTO class for Region
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public class RegionDTO {

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
}
