/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;

import java.io.Serializable;

/**
 * VO class for Role
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class RoleVO extends AbstractVO<String> implements Serializable {

    private static final long serialVersionUID = 1564574233487923178L;

    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    private String superior;
    /**
     * 用户数
     */
    private long count;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
