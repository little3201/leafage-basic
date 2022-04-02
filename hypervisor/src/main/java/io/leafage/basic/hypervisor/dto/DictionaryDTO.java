/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * DTO class for Dictionary
 *
 * @author liwenqiang 2022-03-30 07:26
 **/
public class DictionaryDTO implements Serializable {

    private static final long serialVersionUID = 8881522006869904709L;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
