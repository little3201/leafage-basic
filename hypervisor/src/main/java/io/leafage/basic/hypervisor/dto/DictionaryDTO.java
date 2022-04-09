/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String name;
    /**
     * 简称
     */
    @NotBlank
    private String alias;
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
