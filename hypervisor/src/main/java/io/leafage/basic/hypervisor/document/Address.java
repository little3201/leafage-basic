/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for Address
 *
 * @author liwenqiang 2020-10-06 22:09
 **/
@Document(collection = "address")
public class Address extends BaseDocument {

    /**
     * 国家
     */
    private String country;
    /**
     * 省/直辖市
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县/区
     */
    private String region;
    /**
     * 街道
     */
    private String street;
    /**
     * 地址
     */
    private String address;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
