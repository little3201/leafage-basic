/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * BO class
 *
 * @author liwenqiang 2022-12-09 22:23
 */
public abstract class SuperBO<T> {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;
    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String alias;
    /**
     * 上级
     */
    private BasicBO<T> superior;
    /**
     * 描述
     */
    @Size(max = 32)
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

    public BasicBO<T> getSuperior() {
        return superior;
    }

    public void setSuperior(BasicBO<T> superior) {
        this.superior = superior;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
