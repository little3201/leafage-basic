/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

/**
 * VO class
 *
 * @author liwenqiang 2022-12-09 22:23
 */
public abstract class SuperVO<T> extends AbstractVO<T> {

    /**
     * 名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 上级
     */
    private BasicVO<T> superior;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public BasicVO<T> getSuperior() {
        return superior;
    }

    public void setSuperior(BasicVO<T> superior) {
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
