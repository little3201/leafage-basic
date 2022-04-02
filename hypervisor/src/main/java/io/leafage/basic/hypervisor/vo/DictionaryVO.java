/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;

import java.io.Serializable;

/**
 * VO class for Dictionary
 *
 * @author liwenqiang 2022-03-30 07:27
 **/
public class DictionaryVO extends AbstractVO<String> implements Serializable {

    private static final long serialVersionUID = 2276936498610904136L;

    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    private String superior;
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

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
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
