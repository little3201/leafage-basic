/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for Region
 *
 * @author liwenqiang 2020-10-06 22:09
 **/
@Document(collection = "region")
public class Region extends AbstractDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private Long code;
    /**
     * 名称
     */
    private String name;
    /**
     * 简称
     */
    private String alias;
    /**
     * 上级
     */
    @Indexed(sparse = true)
    private Long superior;
    /**
     * 邮编
     */
    @Field(value = "postal_code")
    private Integer postalCode;
    /**
     * 描述
     */
    private String description;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

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

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
