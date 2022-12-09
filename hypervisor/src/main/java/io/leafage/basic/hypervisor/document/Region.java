/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for Region
 *
 * @author liwenqiang 2020-10-06 22:09
 **/
@Document(collection = "region")
public class Region extends SuperDocument<Long> {

    /**
     * 邮编
     */
    @Field(value = "postal_code")
    private Integer postalCode;
    /**
     * 区号
     */
    @Field(value = "area_code")
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
