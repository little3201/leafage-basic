/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for Region
 *
 * @author liwenqiang 2020-10-06 22:09
 **/
@Document(collection = "region")
public class Region extends BaseDocument {

    /**
     * 代码
     */
    private Long code;
    /**
     * 国家
     */
    private Integer country;
    /**
     * 名称
     */
    private String name;
    /**
     * 省/直辖市
     */
    private Integer province;
    /**
     * 市
     */
    private Integer city;
    /**
     * 区/街道
     */
    private String area;
    /**
     * 乡/镇
     */
    private Integer town;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getTown() {
        return town;
    }

    public void setTown(Integer town) {
        this.town = town;
    }
}
